package de.htwg.se.othello.controller

import de.htwg.se.othello.controller.GameStatus._
import de.htwg.se.othello.model.{Board, Bot, Player}
import de.htwg.se.othello.util.{Observable, UndoManager}

import scala.util.Success

class Controller(var board: Board, var players: Vector[Player]) extends Observable {

  private val undoManager = new UndoManager
  var player: Player = players(0)
  var gameStatus: GameStatus = IDLE

  def this(players: Vector[Player]) = this(new Board, players)

  def this() = this(Vector(new Player(1), new Bot(2)))

  def setupPlayers(number: String): Unit = number match {
    case "0" => players = Vector(new Bot(1), new Bot(2))
    case "1" => players = Vector(new Player(1), new Bot(2))
    case "2" => players = Vector(new Player(1), new Player(2))
  }

  def newGame(): Unit = {
    board = new Board
    player = players(0)
    notifyObservers()
    selectAndSet()
  }

  def set(square: (Int, Int)): Unit = {
    undoManager.doStep(new SetCommand(square, player.value, this))
    notifyObservers()
    if (options.isEmpty && !board.gameOver) omitPlayer()
    else selectAndSet()
  }

  def selectAndSet(): Unit = {
    if (!board.gameOver && player.isBot) {
      Thread.sleep(0)
      new MoveSelector(this).select match {
        case Success(square) => set(square)
        case _ => omitPlayer()
      }
      selectAndSet()
    }
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

  def options: List[(Int, Int)] = moves.values.flatten.toSet.toList.sorted

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = board.moves(player.value)

  def nextPlayer: Player = if (player == players(0)) players(1) else players(0)

  def boardToString: String = board.toString
}
