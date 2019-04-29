package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import de.htwg.se.othello.util.Observable
import scala.util.Random

class Controller(var board: Board, var p: Vector[Player]) extends Observable {

  var player: Player = p(0)

  def switchPlayer: Player = if (player == p(0)) p(1) else p(0)

  def newGame(): Unit = {
    board = new Board
    player = p(0)
    notifyObservers()
  }

  def boardToString: String = {
    if (!gameOver) board.toString
    else board.toString + "\n" + result + "\n\nPress \"n\" for new game"
  }

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def mapOutput(x: Int, y: Int): String = (x + 65).toChar.toString + (y + 1)

  def gameOver: Boolean = {
    val a = moves.isEmpty
    player = switchPlayer
    val b = moves.isEmpty
    player = switchPlayer
    a && b
  }

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      i <- 0 to 7
      j <- 0 to 7 if setByPlayer(i, j)
    } yield getMoves(i, j)).filter(_._2.nonEmpty).toMap
  }

  def getMoves(x: Int, y: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((x, y), (for {
      i <- -1 to 1
      j <- -1 to 1 if !(i == 0 && j == 0)
    } yield check(x, y, (i, j))).filter(_ != (-1, -1)))
  }

  def check(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX > -1 && nX < 8 && nY > -1 && nY < 8 && setByOpponent(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (-1, -1)
    }
  }

  def checkRec(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setByPlayer(nX, nY)) {
      (-1, -1)
    } else if (setByOpponent(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (nX, nY)
    }
  }

  def botSet(): Unit = {
    if (moves.isEmpty) {
      switchNoMoves()
    } else {
      val move = moves.toList(Random.nextInt(moves.keySet.size))
      val square = move._2(Random.nextInt(move._2.size))
      println(f"$player sets ${mapOutput(square._1, square._2)}")
      set(mapOutput(square._1, square._2))
    }
  }

  def set(input: String): Boolean = {
    if (moves.isEmpty) {
      switchNoMoves()
      false
    } else if (input.length != 2) {
      false
    } else {
      val square = mapToBoard(input)
      val valid = moves.filter(_._2.contains(square))
      if (valid.nonEmpty) {
        for {disk <- valid.keys
        } board = board.flipLine(square, disk, player.value)
        deHighlight()
        player = switchPlayer
        notifyObservers()
        true
      } else {
        false
      }
    }
  }

  def switchNoMoves(): Unit = {
    print(f"No valid moves for $player. ")
    player = switchPlayer
    println(f"$player's turn")
    notifyObservers()
  }

  def highlight(): Unit = {
    if (board.countHighlighted == 0) {
      for {
        square <- moves.values.flatten
      } board = board.highlight(square)
    } else {
      deHighlight()
    }
    notifyObservers()
  }

  def deHighlight(): Unit = {
    for {
      x <- 0 to 7
      y <- 0 to 7 if board.isHighlighted(x, y)
    } board = board.deHighlight((x, y))
  }

  def setByPlayer(x: Int, y: Int): Boolean = board.valueOf(x, y) == player.value

  def setByOpponent(x: Int, y: Int): Boolean = {
    board.isSet(x, y) && board.valueOf(x, y) != player.value
  }

  def suggestions: String = {
    (for {
      e <- moves.values.flatten.toSet.toList.sorted
    } yield mapOutput(e._1, e._2)).mkString(" ")
  }

  def result: String = {
    val p1 = player
    player = switchPlayer
    val p2 = player
    val count = board.countAll(p1.value, p2.value)
    val wCount = count._1 max count._2
    val lCount = count._1 min count._2
    val winner = if (wCount == count._1) p1 else p2
    if (wCount != lCount) f"$winner wins by $wCount:$lCount!"
    else f"Draw. $wCount:$lCount"
  }
}
