package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Bot, Player}
import de.htwg.se.othello.util.Observable

import scala.util.Random.nextInt

class Controller(var board: Board, var p: Vector[Player]) extends Observable {

  var player: Player = p(0)
  var moveIsLegal: Boolean = true

  def this(p: Vector[Player]) = this(new Board, p)

  def setupPlayers(number: String): Unit = {
    number match {
      case "0" => p = Vector(new Bot(1), new Bot(2))
      case "1" => p = Vector(new Player(1), new Bot(2))
      case "2" => p = Vector(new Player(1), new Player(2))
    }
  }

  def newGame(): Unit = {
    board = new Board
    player = p(0)
    notifyObservers()
    if (player.isInstanceOf[Bot]) setAndNext()
  }

  def nextPlayer: Player = if (player == p(0)) p(1) else p(0)

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = board.moves(player.value)

  def setAndNext(): Unit = {
    Thread.sleep(0)
    select match {
      case Some(selection) => set(selection)
      case None =>
        player = nextPlayer
        omitted = true
        notifyObservers()
    }
    if (player.isInstanceOf[Bot] && !gameOver) setAndNext()
  }

  var omitted: Boolean = false

  def set(toSquare: (Int, Int)): Unit = {
    if (moves.nonEmpty) {
      val legal = moves.filter(o => o._2.contains(toSquare))
      moveIsLegal = legal.nonEmpty
      for {
        fromSquare <- legal.keys
      } board = board.flipLine(fromSquare, toSquare, player.value)
      board = board.deHighlight
      if (moveIsLegal) player = nextPlayer
    } else {
      player = nextPlayer
      omitted = true
    }
    notifyObservers()
  }

  def highlight(): Unit = {
    if (!board.isHighlighted) {
      for {
        (col, row) <- moves.values.flatten
      } board = board.highlight(col, row)
    } else board = board.deHighlight
    notifyObservers()
  }

  def select: Option[(Int, Int)] = {
    try {
      val move = moves.toList(nextInt(moves.keySet.size))
      Some(move._2(nextInt(move._2.size)))
    } catch {
      case _: IllegalArgumentException => None
    }
  }

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def suggestions: String = {
    (for {
      (col, row) <- moves.values.flatten.toSet.toList.sorted
    } yield (col + 65).toChar.toString + (row + 1)).mkString(" ")
  }

  def score: String = {
    val count = board.countAll(p(0).value, p(1).value)
    val (winCount, loseCount) = (count._1 max count._2, count._1 min count._2)
    val winner = if (winCount == count._1) p(0) else p(1)
    if (winCount != loseCount) f"$winner wins by $winCount:$loseCount!"
    else f"Draw. $winCount:$loseCount"
  }

  def gameOver: Boolean = board.gameOver

  def status: String = {
    if (board.gameOver) board.toString + score + "\n\nPress \"n\" for new game"
    else if (omitted) {
      omitted = false
      s"$player's turn.\n" + board.toString
    } else if (!moveIsLegal) {
      moveIsLegal = true
      suggestions + "\n" + board.toString
    } else board.toString
  }
}
