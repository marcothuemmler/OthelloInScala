package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import de.htwg.se.othello.util.Observable
import scala.util.Random.nextInt

class Controller(var board: Board, var p: Vector[Player]) extends Observable {

  var player: Player = p(0)
  var notLegal: Boolean = false

  def this(p: Vector[Player]) = this(new Board, p)

  def newGame(): Unit = {
    board = new Board
    player = p(0)
    notifyObservers()
  }

  def switchPlayer(): Unit = player = if (player == p(0)) p(1) else p(0)

  def noMoves: Boolean = moves.isEmpty

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      x <- 0 to 7
      y <- 0 to 7 if setByPl(x, y)
    } yield getMoves(x, y)).filter(o => o._2.nonEmpty).toMap
  }

  def getMoves(x: Int, y: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((x, y), (for {
      i <- -1 to 1
      j <- -1 to 1 if !(i == 0 && j == 0)
    } yield check(x, y, (i, j))).filter(o => o != (-1, -1)))
  }

  def check(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX > -1 && nX < 8 && nY > -1 && nY < 8 && setByOpp(nX, nY)) {
      checkRecursive(nX, nY, direction)
    } else (-1, -1)
  }

  def checkRecursive(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setByPl(nX, nY)) (-1, -1)
    else if (setByOpp(nX, nY)) checkRecursive(nX, nY, direction)
    else (nX, nY)
  }

  def setByPl(x: Int, y: Int): Boolean = board.valueOf(x, y) == player.value

  def setByOpp(x: Int, y: Int): Boolean = board.isSet(x, y) && !setByPl(x, y)

  def botSet(): Unit = {
    if (!noMoves) {
      val move = moves.toList(nextInt(moves.keySet.size))
      val square = move._2(nextInt(move._2.size))
      set(square)
    } else notifyObservers()
  }

  def set(square: (Int, Int)): Unit = {
    if (!noMoves) {
      val legal = moves.filter(o => o._2.contains(square))
      if (legal.isEmpty) notLegal = true
      else {
        for {
          disk <- legal.keys
        } board = board.flipLine(square, disk, player.value)
        board = board.deHighlight
        switchPlayer()
      }
    }
    notifyObservers()
  }

  def highlight(): Unit = {
    if (!board.isHighlighted) {
      for {
        (x, y) <- moves.values.flatten
      } board = board.highlight(x, y)
    } else board = board.deHighlight
    notifyObservers()
  }

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def gameOver: Boolean = {
    val a = moves.isEmpty
    switchPlayer()
    val b = moves.isEmpty
    switchPlayer()
    a && b
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

  def boardToString: String = {
    if (noMoves && !gameOver) {
      val str = s"No moves for $player. "
      switchPlayer()
      str + s"$player's turn.\n ${board.toString}"
    } else if (notLegal) {
      notLegal = false
      s"Valid moves for $player: $suggestions\n${board.toString}"
    } else if (gameOver) {
      board.toString + "\n" + score + "\n\nPress \"n\" for new game"
    } else board.toString
  }
}
