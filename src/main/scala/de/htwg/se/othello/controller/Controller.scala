package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import de.htwg.se.othello.util.Observable
import scala.util.Random

class Controller(var board: Board, var p: Vector[Player]) extends Observable {

  var player: Player = p(0)
  var notValid = false

  def this(p: Vector[Player]) = this(new Board, p)

  def newGame(): Unit = {
    board = new Board
    player = p(0)
    notifyObservers()
  }

  def switchPlayer(): Unit = player = if (player == p(0)) p(1) else p(0)

  def noMoves: Boolean = moves.isEmpty

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def mapOutput(square: (Int, Int)): String = {
    (square._1 + 65).toChar.toString + (square._2 + 1)
  }

  def gameOver: Boolean = {
    val a = moves.isEmpty
    switchPlayer()
    val b = moves.isEmpty
    switchPlayer()
    a && b
  }

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

  def botSet(): Unit = {
    if (noMoves) notifyObservers()
    else {
      val move = moves.toList(Random.nextInt(moves.keySet.size))
      val square = move._2(Random.nextInt(move._2.size))
      set(square)
    }
  }

  def set(square: (Int, Int)): Unit = {
    if (noMoves) notifyObservers()
    else {
      val valid = moves.filter(o => o._2.contains(square))
      if (valid.isEmpty) notValid = true
      else {
        for {
          disk <- valid.keys
        } board = board.flipLine(square, disk, player.value)
        deHighlight()
        switchPlayer()
      }
      notifyObservers()
    }
  }

  def highlight(): Unit = {
    if (board.countHighlighted == 0) {
      for {
        square <- moves.values.flatten
      } board = board.highlight(square)
    } else deHighlight()
    notifyObservers()
  }

  def deHighlight(): Unit = {
    for {
      x <- 0 to 7
      y <- 0 to 7 if board.isHighlighted(x, y)
    } board = board.deHighlight((x, y))
  }

  def setByPl(x: Int, y: Int): Boolean = board.valueOf(x, y) == player.value

  def setByOpp(x: Int, y: Int): Boolean = board.isSet(x, y) && !setByPl(x, y)

  def suggestions: String = {
    (for {
      square <- moves.values.flatten.toSet.toList.sorted
    } yield mapOutput(square)).mkString(" ")
  }

  def boardToString: String = {
    if (noMoves && !gameOver) {
      val str = s"No moves for $player. "
      switchPlayer()
      str + s"$player's turn.\n ${board.toString}"
    } else if (notValid) {
      notValid = false
      s"Valid moves for $player: $suggestions\n ${board.toString}"
    } else if (gameOver) {
      board.toString + "\n" + result + "\n\nPress \"n\" for new game"
    } else board.toString
  }

  def result: String = {
    val player1 = player
    switchPlayer()
    val player2 = player
    val count = board.countAll(player1.value, player2.value)
    val (winCount, loseCount) = (count._1 max count._2, count._1 min count._2)
    val winner = if (winCount == count._1) player1 else player2
    if (winCount != loseCount) f"$winner wins by $winCount:$loseCount!"
    else f"Draw. $winCount:$loseCount"
  }
}
