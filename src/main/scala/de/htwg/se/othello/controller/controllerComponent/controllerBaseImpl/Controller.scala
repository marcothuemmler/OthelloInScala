package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.controller.controllerComponent.{BoardControllerInterface, ControllerInterface}
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import de.htwg.se.othello.model.{Bot, Player}
import de.htwg.se.othello.util.UndoManager
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}

class Controller extends ControllerInterface {

  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val injector: Injector = Guice.createInjector(new OthelloModule)
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  val boardController: BoardControllerInterface = injector.getInstance(classOf[BoardControllerInterface])
  val boardModuleURL: String = "http://localhost:8081/boardmodule"
  val userModuleURL: String = "http://localhost:8082/usermodule"
  private val undoManager = new UndoManager
  var gameStatus: GameStatus = IDLE
  var difficulty = "Normal"

  def resizeBoard(op: String): Unit = {
    Http().singleRequest(Post(s"$userModuleURL/resetplayer"))
    boardController.resizeBoard(op)
    notifyObservers()
  }

  def size: Int = boardController.size

  def createBoard(size: Int): Unit = {
    boardController.createBoard(size)
    notifyObservers()
  }

  def illegalAction(): Unit = {
    gameStatus = ILLEGAL
    publishChanges()
  }

  def publishChanges(): Unit = {
    notifyObservers()
    gameStatus = IDLE
  }

  def setupPlayers: String => Unit = input => {
    Http().singleRequest(Post(s"$userModuleURL/setupplayers/$input"))
  }

  def moveSelector: MoveSelector = difficulty match {
    case "Easy" => new EasyBot(this)
    case "Normal" => new MediumBot(this)
    case "Hard" => new HardBot(this)
  }

  def setDifficulty(value: String): Unit = {
    difficulty = value match {
      case "e" => "Easy"
      case "m" => "Normal"
      case "d" => "Hard"
    }
    gameStatus = DIFFICULTY_CHANGED
    publishChanges()
  }

  def newGame: Future[Unit] = {
    undoManager.redoStack = Nil
    undoManager.undoStack = Nil
    createBoard(size)
    Http().singleRequest(Post(s"$userModuleURL/resetplayer"))
    Future(selectAndSet())(ExecutionContext.global)
  }

  def save(): Unit = fileIo.save(boardController.board, getCurrentPlayer, difficulty)

  def getCurrentPlayer: Player = {
    val response = Http().singleRequest(Get(s"$userModuleURL/getcurrentplayer"))
    playerFromHttpResponse(response)
  }

  def playerFromHttpResponse(response: Future[HttpResponse]): Player = {
    val responseBody = response.flatMap(r => Unmarshal(r.entity).to[String])
    val result = Await.result(responseBody, Duration.Inf)
    val playerJson: JsValue = Json.parse(result)
    val name = (playerJson \ "name").as[String]
    val color = (playerJson \ "value").as[Int]
    if ((playerJson \ "isBot").as[Boolean]) {
      new Bot(name, color)
    } else {
      Player(name, color)
    }
  }

  def load(): Unit = {
    fileIo.load match {
      case scala.util.Success(save) =>
        boardController.board = save._1
        setCurrentPlayer(save._2)
        difficulty = save._3
        gameStatus = LOAD_SUCCESS
      case _ => gameStatus = LOAD_FAIL
    }
    publishChanges()
  }

  def setCurrentPlayer(player: Player): Unit = {
    val playerJson = player.toJson
    val name = (playerJson \ "name").as[String]
    val color = (playerJson \ "value").as[Int]
    val isBot = (playerJson \ "isBot").as[Boolean]
    Http().singleRequest(Post(s"$userModuleURL/setcurrentplayer/?name=$name&value=$color&isBot=$isBot"))
  }

  def set(square: (Int, Int)): Unit = {
    if (!moves.exists(o => o._2.contains(square))) {
      gameStatus = ILLEGAL
      boardController.changeHighlight
    } else if (getCurrentPlayer.isBot) new SetCommand(square, this).doStep()
    else undoManager.doStep(new SetCommand(square, this))
    if (gameOver) gameStatus = GAME_OVER
    publishChanges()
    if (!gameOver && moves.isEmpty) omitPlayer() else selectAndSet()
  }

  def selectAndSet(): Unit = if (getCurrentPlayer.isBot && !gameOver) {
    if (moves.nonEmpty) set(moveSelector.selection) else omitPlayer()
    selectAndSet()
  }

  def omitPlayer(): Unit = {
    setCurrentPlayer(nextPlayer)
    gameStatus = OMITTED
    publishChanges()
  }

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def highlight(): Unit = {
    boardController.changeHighlight
    notifyObservers()
  }

  def suggestions: String = {
    options.map(o => (o._1 + 65).toChar.toString + (o._2 + 1)).mkString(" ")
  }

  def options: Seq[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = boardController.moves

  def gameOver: Boolean = boardController.gameOver

  def nextPlayer: Player = {
    val response = Http().singleRequest(Get(s"$userModuleURL/nextplayer"))
    playerFromHttpResponse(response)
  }

  def playerCount: Int = {
    val response = Http().singleRequest(Get(s"$userModuleURL/playercount"))
    val responseBody = response.flatMap(r => Unmarshal(r.entity).to[String])
    Await.result(responseBody, Duration.Inf).toInt
  }

  def boardToString: String = boardController.boardToString

  def boardToHtml: String = boardController.boardToHtml

  def valueOf(col: Int, row: Int): Int = boardController.valueOf(col, row)

  def canUndo: Boolean = undoManager.undoStack.nonEmpty

  def canRedo: Boolean = undoManager.redoStack.nonEmpty

  implicit def currentPlayerValue: Int = getCurrentPlayer.value

  def score: String = {
    val score1 = count(1)
    val score2 = count(2)
    val (win, lose) = (score1 max score2, score1 min score2)
    val winner = getPlayer(win == score1)
    if (win != lose) f"$winner wins by $win:$lose!" else f"Draw. $win:$lose"
  }

  def getPlayer(isFirstPlayer: Boolean): Player = {
    val response = Http().singleRequest(Get(s"$userModuleURL/getplayer/?isfirstplayer=$isFirstPlayer"))
    playerFromHttpResponse(response)
  }

  def count(value: Int): Int = boardController.count(value)
}
