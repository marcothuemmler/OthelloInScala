package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.controller.controllerComponent._
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import de.htwg.se.othello.model.{Bot, Player}
import de.htwg.se.othello.util.UndoManager
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class Controller(var board: BoardInterface, var players: Vector[Player]) extends ControllerInterface {

  private val undoManager = new UndoManager
  var player: Player = players(0)
  var gameStatus: GameStatus = IDLE
  var difficulty = 2
  val injector: Injector = Guice.createInjector(new OthelloModule)
  val fileIo: FileIOInterface = injector.instance[FileIOInterface]

  def this(players: Vector[Player]) = this(
    Guice.createInjector(new OthelloModule).instance[BoardFactory].create(8),
    players
  )

  def this() = this(Vector(new Player(1), new Bot(2)))

  def resizeBoard(op: String): Unit = {
    player = players(0)
    op match {
      case "+" => createBoard(size + 2)
      case "-" => if (size > 4) createBoard(size - 2)
      case "." => if (size != 8) createBoard(8)
    }
  }

  def size: Int = board.size

  def createBoard(size: Int): Unit = {
    board = injector.instance[BoardFactory].create(size)
    board = (new CreateBoardStrategy).fill(board)
    publish(new BoardChanged)
  }

  def setupPlayers: String => Unit = {
    case "0" => players = Vector(new Bot(1), new Bot(2))
    case "1" => players = Vector(new Player(1), new Bot(2))
    case "2" => players = Vector(new Player(1), new Player(2))
  }

  def moveSelector: Int => MoveSelector = {
    case 1 => new EasyBot(this)
    case 2 => new MediumBot(this)
    case 3 => new HardBot(this)
  }

  def setDifficulty(value: String): Unit = {
    value match {
      case "e" => difficulty = 1
      case "m" => difficulty = 2
      case "d" => difficulty = 3
    }
    gameStatus = DIFFICULTY_CHANGED
    publish(new BoardChanged)
  }

  def newGame: Future[Unit] = {
    createBoard(board.size)
    player = players(0)
    Future(selectAndSet())(ExecutionContext.global)
  }

  def save(): Unit = fileIo.save(board, player, difficulty)

  def load(): Unit = {
    fileIo.load match {
      case Success(savegame) =>
        board = savegame._1
        player = savegame._2
        difficulty = savegame._3
        gameStatus = LOAD_SUCCESS
        publish (new BoardChanged)
      case _ =>
        gameStatus = LOAD_FAIL
        publish(new BoardChanged)
    }
  }

  def set(square: (Int, Int)): Unit = {
    if (!moves.exists(o => o._2.contains(square))) gameStatus = ILLEGAL
    else undoManager.doStep(new SetCommand(square, player.value, this))
    if (gameOver) gameStatus = GAME_OVER
    publish(new BoardChanged)
    if (!gameOver && moves.isEmpty) omitPlayer()
    else selectAndSet()
  }

  @tailrec
  final def selectAndSet(): Unit = if (player.isBot && !gameOver) {
    moveSelector(difficulty).select match {
      case Success(square) => set(square)
      case _ => omitPlayer()
    }
    selectAndSet()
  }

  def omitPlayer(): Unit = {
    gameStatus = OMITTED
    publish(new PlayerOmitted(player))
    player = nextPlayer
  }

  def undo(): Unit = {
    undoManager.undoStep()
    undoManager.undoStep()
    publish(new BoardChanged)
  }

  def redo(): Unit = {
    undoManager.redoStep()
    undoManager.redoStep()
    publish(new BoardChanged)
  }

  def highlight(): Unit = {
    board = board.changeHighlight(player.value)
    publish(new BoardChanged)
  }

  def suggestions: String = {
    (for { (col, row) <- options }
      yield (col + 65).toChar.toString + (row + 1)).mkString(" ")
  }

  def options: Seq[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = board.moves(player.value)

  def gameOver: Boolean = board.gameOver

  def nextPlayer: Player = if (player == players(0)) players(1) else players(0)

  def playerPresent: Int = players.indexOf(player)

  def playerCount: Int = players.count(o => !o.isBot)

  def boardToString: String = board.toString

  def valueOf(col: Int, row: Int): Int = board.valueOf(col, row)

  def score: String = {
    val (win, lose) = (count(1) max count(2), count(1) min count(2))
    val winner = if (win == count(1)) players(0) else players(1)
    if (win != lose) f"$winner wins by $win:$lose!" else f"Draw. $win:$lose"
  }

  def count(value: Int): Int = board.count(value)
}
