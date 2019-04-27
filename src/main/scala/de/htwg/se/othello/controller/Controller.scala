package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import de.htwg.se.othello.util.Observable

import scala.util.Random

class Controller(var board: Board, var players: Vector[Player]) extends Observable {

  var player: Player = players(0)

  def newGame(): Unit = {
    board = new Board
    player = players(0)
    notifyObservers()
  }

  def boardToString: String = board.toString

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

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
      j <- 0 to 7 if setByPl(i, j)
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
    if (nX > -1 && nX < 8 && nY > -1 && nY < 8 && setByOpp(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (-1, -1)
    }
  }

  def checkRec(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setByPl(nX, nY)) {
      (-1, -1)
    } else if (setByOpp(nX, nY)) {
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
    print(f"No possible moves for $player. ")
    player = switchPlayer
    println(f"$player's turn")
    notifyObservers()
  }

  def deHighlight(): Unit = {
    for {
      x <- 0 to 7
      y <- 0 to 7 if board.grid(x)(y).value < 0
    } board = board.flip((x, y), 0)
  }

  def highlight(): Unit = {
    for {disk <- moves.values.flatten
    } board = board.flip(disk, -1)
    notifyObservers()
  }

  def setByPl(x: Int, y: Int): Boolean = board.grid(x)(y).value == player.value

  def setByOpp(x: Int, y: Int): Boolean = {
    board.grid(x)(y).value > 0 && board.grid(x)(y).value != player.value
  }

  def suggestions: String = {
    s"Possible moves for $player: " +
      (for {e <- moves.values.flatten.toSet.toList.sorted
      } yield mapOutput(e._1, e._2)).mkString(" ")
  }

  def result: String = {
    val p1 = player
    val p1count = count
    player = switchPlayer
    val p2 = player
    val p2count = count
    val winner = if (p1count >= p2count) p1 else p2
    val wCount = if (p1count > p2count) p1count else p2count
    val lCount = if (wCount == p1count) p2count else p1count
    val str = if (p1count != p2count) {
      f"$winner wins by $wCount:$lCount!"
    } else {
      f"Draw. $p1count:$p2count"
    }
    str + "\nPress \"n\" for new game"
  }

  def switchPlayer: Player = {
    if (player == players(0)) players(1) else players(0)
  }

  def count: Int = board.grid.flatten.count(_.value == player.value)

  def mapOutput(x: Int, y: Int): String = (x + 65).toChar.toString + (y + 1)
}
