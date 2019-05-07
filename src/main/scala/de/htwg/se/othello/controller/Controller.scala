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
    if (player.isInstanceOf[Bot]) setAndNext(select.get)
  }

  def nextPlayer: Player = if (player == p(0)) p(1) else p(0)

  def setAndNext(square: (Int, Int)): Unit = {
    set(square)
    if (moveIsLegal) player = nextPlayer
    notifyObservers()
    if (player.isInstanceOf[Bot]) {
      Thread.sleep(500)
      select match {
        case Some(selection) => setAndNext(selection)
        case None =>
      }
    }
  }

  def set(toSquare: (Int, Int)): Unit = {
    val legal = moves.filter(o => o._2.contains(toSquare))
    moveIsLegal = legal.nonEmpty
    for {
      fromSquare <- legal.keys
    } board = board.flipLine(fromSquare, toSquare, player.value)
    board = board.deHighlight
  }

  def highlight(): Unit = {
    if (!board.isHighlighted) {
      for {
        (col, row) <- moves.values.flatten
      } board = board.highlight(col, row)
    } else board = board.deHighlight
    notifyObservers()
  }

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      col <- 0 to 7
      row <- 0 to 7 if setByPl(col, row)
    } yield getMoves(col, row)).filter(o => o._2.nonEmpty).toMap
  }

  def getMoves(col: Int, row: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((col, row), (for {
      x <- -1 to 1
      y <- -1 to 1
      (nX, nY) = (col + x, row + y)
      if !(nX < 0 || nX > 7 || nY < 0 || nY > 7) && setByOpp(nX, nY)
    } yield checkRecursive(nX, nY, (x, y))).filter(o => o != (-1, -1)))
  }

  def checkRecursive(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setByPl(nX, nY)) (-1, -1)
    else if (setByOpp(nX, nY)) checkRecursive(nX, nY, direction)
    else (nX, nY)
  }

  def setByPl(x: Int, y: Int): Boolean = board.valueOf(x, y) == player.value

  def setByOpp(x: Int, y: Int): Boolean = board.isSet(x, y) && !setByPl(x, y)

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

  def gameOver: Boolean = {
    val a = moves.isEmpty
    player = nextPlayer
    val b = moves.isEmpty
    player = nextPlayer
    a && b
  }

  def suggestions: String = {
    s"Valid moves for $player: " + (for {
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

  def status: String = {
    if (gameOver) board.toString + score + "\n\nPress \"n\" for new game"
    else if (moves.isEmpty) {
      val previousPlayer = player
      player = nextPlayer
      s"No valid moves for $previousPlayer. $player's turn.\n" + board.toString
    } else if (!moveIsLegal) {
      moveIsLegal = true
      suggestions + "\n" + board.toString
    } else board.toString
  }
}
