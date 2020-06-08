package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import de.htwg.se.othello.model.{Bot, Player}
import de.htwg.se.othello.util.UndoManager
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}

class Controller extends ControllerInterface {

  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val injector: Injector = Guice.createInjector(new OthelloModule)
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  private val undoManager = new UndoManager
  var gameStatus: GameStatus = IDLE
  var difficulty = "Normal"

  val isDockerEnv: Boolean = sys.env.contains("DOCKER_ENV")
  val boardHost: String = if (isDockerEnv) "boardmodule" else "localhost"
  val userHost: String = if (isDockerEnv) "usermodule" else "localhost"

  val boardModuleURL: String = s"http://$boardHost:8081/boardmodule"
  val userModuleURL: String = s"http://$userHost:8082/usermodule"

  implicit def controller: Controller = this

  def resizeBoard(op: String): Unit = {
    val param = URLEncoder.encode(op, "UTF8")
    Http().singleRequest(Post(s"$boardModuleURL/resize/?op=$param"))
    Http().singleRequest(Post(s"$userModuleURL/resetplayer"))
    notifyObservers()
  }

  def size: Int = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/size"))
    responseString(response).toInt
  }

  def createBoard(size: Int): Unit = {
    Http().singleRequest(Post(s"$boardModuleURL/create/?size=$size"))
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
    case "Easy" => new EasyBot
    case "Normal" => new MediumBot
    case "Hard" => new HardBot
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

  def getBoard: BoardInterface = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/boardjson"))
    val boardJson = Json.parse(responseString(response))
    var board = injector.instance[BoardFactory].create(size)
    for {
      index <- 0 until size * size
      row = (boardJson \\ "row") (index).as[Int]
      col = (boardJson \\ "col") (index).as[Int]
      value = (boardJson \\ "value") (index).as[Int]
    } board = board.flipLine((row, col), (row, col))(value)
    board
  }

  def save(): Unit = fileIo.save(getBoard, currentPlayer, difficulty)

  def currentPlayer: Player = {
    val response = Http().singleRequest(Get(s"$userModuleURL/getcurrentplayer"))
    playerFromHttpResponse(response)
  }

  def playerFromHttpResponse(response: Future[HttpResponse]): Player = {
    val playerJson = Json.parse(responseString(response))
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
        setBoard(save._1)
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
    val result = Http().singleRequest(Post(s"$userModuleURL/setcurrentplayer/?name=$name&value=$color&isBot=$isBot"))
    Await.result(result, Duration.Inf)
  }

  def setBoard(board: BoardInterface): Unit = {
    val result = Http().singleRequest(Post(s"$boardModuleURL/set", board.toJson.toString))
    Await.result(result, Duration.Inf)
  }

  def set(square: (Int, Int)): Unit = {
    if (!moves.exists(_._2.contains(square))) {
      gameStatus = ILLEGAL
      highlight()
    } else if (currentPlayer.isBot) SetCommand(square).doStep()
    else undoManager.doStep(SetCommand(square))
    if (gameOver) gameStatus = GAME_OVER
    publishChanges()
    if (!gameOver && moves.isEmpty) omitPlayer() else selectAndSet()
  }

  def selectAndSet(): Unit = if (currentPlayer.isBot && !gameOver) {
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
    Http().singleRequest(Post(s"$boardModuleURL/changehighlight/?value=${currentPlayer.value}"))
    notifyObservers()
  }

  def suggestions: String = {
    options.map(o => (o._1 + 65).toChar.toString + (o._2 + 1)).mkString(" ")
  }

  def options: Seq[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/moves/?value=${currentPlayer.value}"))
    val movesJson = Json.parse(responseString(response))
    val movesList = movesJson.as[List[JsValue]]
    movesList.map(e =>
      (e \ "key").as[(Int, Int)] -> (e \ "value").as[Seq[(Int, Int)]]).toMap
  }

  def gameOver: Boolean = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/gameover"))
    responseString(response).toBoolean
  }

  def nextPlayer: Player = {
    val response = Http().singleRequest(Get(s"$userModuleURL/nextplayer"))
    playerFromHttpResponse(response)
  }

  def playerCount: Int = {
    val response = Http().singleRequest(Get(s"$userModuleURL/playercount"))
    responseString(response).toInt
  }

  def boardToString: String = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/boardstring"))
    responseString(response)
  }

  def boardToHtml: String = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/boardhtml"))
    responseString(response)
  }

  def valueOf(col: Int, row: Int): Int = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/valueof/?col=$col&row=$row"))
    responseString(response).toInt
  }

  def responseString(response: Future[HttpResponse]): String = {
    val responseBody = response.flatMap(r => Unmarshal(r.entity).to[String])
    Await.result(responseBody, Duration.Inf)
  }

  def canUndo: Boolean = undoManager.undoStack.nonEmpty

  def canRedo: Boolean = undoManager.redoStack.nonEmpty

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

  def count(value: Int): Int = {
    val response = Http().singleRequest(Get(s"$boardModuleURL/count/?value=$value"))
    responseString(response).toInt
  }
}
