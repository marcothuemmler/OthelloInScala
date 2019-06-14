package de.htwg.se.othello.controller

import de.htwg.se.othello.controller.GameStatus._
import de.htwg.se.othello.model.{Board, Bot, CreateBoardStrategy, Player}
import de.htwg.se.othello.util.{Observable, UndoManager}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class Controller(var board: Board, var players: Vector[Player]) extends Observable {

  private val undoManager = new UndoManager
  var player: Player = players(0)
  var gameStatus: GameStatus = IDLE
  var isReady = true

  def this(players: Vector[Player]) = this(new Board, players)

  def this() = this(Vector(new Player(1), new Bot(2)))

  def this(size: Int) = this(new Board(size), Vector(new Player(1), new Bot(2)))

  def resizeBoard(op: String): Unit = {
    player = players(0)
    op match {
      case "+" => createBoard(size + 2)
      case "-" => if (size > 4) createBoard(size - 2)
      case "." => if (size != 8) createBoard(8)
    }
  }

  def createBoard(size: Int): Unit = {
    board = (new CreateBoardStrategy).createNewBoard(size)
    notifyObservers()
  }

  def setupPlayers(number: String): Unit = number match {
    case "0" => players = Vector(new Bot(1), new Bot(2))
    case "1" => players = Vector(new Player(1), new Bot(2))
    case "2" => players = Vector(new Player(1), new Player(2))
  }

  def newGame(): Unit = {
    createBoard(board.size)
    player = players(0)
    Future(selectAndSet())(ExecutionContext.global)
  }

  def set(square: (Int, Int)): Unit = {
    if (moves.filter(o => o._2.contains(square)).keys.isEmpty) {
      gameStatus = ILLEGAL
    } else undoManager.doStep(new SetCommand(square, player.value, this))
    if (gameOver) gameStatus = GAME_OVER
    notifyObservers()
    if (!gameOver && moves.isEmpty) omitPlayer()
    else selectAndSet()
  }

  def selectAndSet(): Unit = if (player.isBot && !gameOver) {
    isReady = false
    new MoveSelector(this).select() match {
      case Success(square) => set(square)
      case _ => omitPlayer()
    }
    isReady = true
    selectAndSet()
  }

  def omitPlayer(): Unit = {
    player = nextPlayer
    gameStatus = OMITTED
    notifyObservers()
  }

  def undo(): Unit = {
    undoManager.undoStep()
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    undoManager.redoStep()
    notifyObservers()
  }

  def highlight(): Unit = {
    board = {
      if (board.isHighlighted) board.deHighlight
      else board.highlight(player.value)
    }
    notifyObservers()
  }

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def suggestions: String = {
    (for { (col, row) <- options }
      yield (col + 65).toChar.toString + (row + 1)).mkString(" ")
  }

  def size: Int = board.size

  def options: Seq[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = board.moves(player.value)

  def gameOver: Boolean = board.gameOver

  def nextPlayer: Player = if (player == players(0)) players(1) else players(0)

  def playerCount: Int = players.size - players.count(o => o.isBot)

  def boardToString: String = board.toString
}
