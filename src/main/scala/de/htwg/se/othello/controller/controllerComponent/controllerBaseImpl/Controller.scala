package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.controller.UserControllerInterface
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.controller.BoardControllerInterface
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import de.htwg.se.othello.util.UndoManager

import scala.concurrent.{ExecutionContext, Future}

class Controller extends ControllerInterface {

  val injector: Injector = Guice.createInjector(new OthelloModule)
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  val userController: UserControllerInterface = injector.getInstance(classOf[UserControllerInterface])
  val boardController: BoardControllerInterface = injector.getInstance(classOf[BoardControllerInterface])
  private val undoManager = new UndoManager
  var gameStatus: GameStatus = IDLE
  var difficulty = "Normal"

  def resizeBoard(op: String): Unit = {
    userController.resetPlayer
    boardController.resizeBoard(op)
    notifyObservers()
  }

  def size: Int = boardController.board.size

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

  def getCurrentPlayer: Player = userController.getCurrentPlayer

  def setupPlayers: String => Unit = userController.setupPlayers

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
    userController.resetPlayer
    Future(selectAndSet())(ExecutionContext.global)
  }

  def save(): Unit = fileIo.save(boardController.board, userController.getCurrentPlayer, difficulty)

  def load(): Unit = {
    fileIo.load match {
      case scala.util.Success(save) =>
        boardController.board = save._1
        userController.setCurrentPlayer(save._2)
        difficulty = save._3
        gameStatus = LOAD_SUCCESS
      case _ => gameStatus = LOAD_FAIL
    }
    publishChanges()
  }

  def set(square: (Int, Int)): Unit = {
    if (!moves.exists(o => o._2.contains(square))) {
      gameStatus = ILLEGAL
      boardController.board = boardController.board.changeHighlight(userController.getCurrentPlayer.value)
    } else if (userController.getCurrentPlayer.isBot) new SetCommand(square, this).doStep()
    else undoManager.doStep(new SetCommand(square, this))
    if (gameOver) gameStatus = GAME_OVER
    publishChanges()
    if (!gameOver && moves.isEmpty) omitPlayer() else selectAndSet()
  }

  def selectAndSet(): Unit = if (userController.getCurrentPlayer.isBot && !gameOver) {
    if (moves.nonEmpty) set(moveSelector.selection) else omitPlayer()
    selectAndSet()
  }

  def omitPlayer(): Unit = {
    userController.setCurrentPlayer(nextPlayer)
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
    boardController.board = boardController.board.changeHighlight(userController.getCurrentPlayer.value)
    notifyObservers()
  }

  def suggestions: String = {
    options.map(o => (o._1 + 65).toChar.toString + (o._2 + 1)).mkString(" ")
  }

  def options: Seq[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = boardController.board.moves(userController.getCurrentPlayer.value)

  def gameOver: Boolean = boardController.board.gameOver

  def nextPlayer: Player = userController.nextPlayer

  def playerCount: Int = userController.playerCount

  def boardToString: String = boardController.boardToString

  def boardToHtml: String = boardController.boardToHtml

  def valueOf(col: Int, row: Int): Int = boardController.board.valueOf(col, row)

  def canUndo: Boolean = undoManager.undoStack.nonEmpty

  def canRedo: Boolean = undoManager.redoStack.nonEmpty

  def score: String = {
    val score1 = count(1)
    val score2 = count(2)
    val (win, lose) = (score1 max score2, score1 min score2)
    val winner = userController.getPlayer(win == score1)
    if (win != lose) f"$winner wins by $win:$lose!" else f"Draw. $win:$lose"
  }

  def count(value: Int): Int = boardController.board.count(value)
}
