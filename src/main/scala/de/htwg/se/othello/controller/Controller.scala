package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import de.htwg.se.othello.util.Observable

import scala.util.Random

class Controller(var board: Board, var players: Vector[Player]) extends Observable {

  var i = 0
  var current: Player = players(i)

  def newGame(): Unit = notifyObservers()

  def boardToString: String = board.toString

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }

  def changeCurrent(i: Int): Int = if (i == 0) 1 else 0

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

  def set(input: String): Boolean = {
    val square = mapToBoard(input)
    val allMoves = moves
    val valid = allMoves.filter(_._2.contains(square))
    if (valid.nonEmpty) {
      for {
        disk <- valid.keys
      } board = board.flipLine(square, disk, current.value)
      for {
        e <- allMoves.values.flatten.filter(_ != square)
      } board = board.flip(e, 0)
      i = changeCurrent(i)
      current = players(i)
      notifyObservers()
      true
    } else {
      false
    }
  }

  def highlight(): Unit = {
    for {disk <- moves.values.flatten} board = board.flip(disk, -1)
    notifyObservers()
  }

  def setByPl(x: Int, y: Int): Boolean = board.grid(x)(y).value == current.value

  def setByOpp(x: Int, y: Int): Boolean = {
    board.grid(x)(y).value > 0 && board.grid(x)(y).value != current.value
  }

  def count: Int = board.grid.flatten.count(_.value == current.value)

  def botSet: String = {
    val move = moves.toList(Random.nextInt(moves.keySet.size))
    val square = move._2(Random.nextInt(move._2.size))
    val out = mapOutput(square._1, square._2)
    println(f"$current sets ${mapOutput(square._1, square._2)}")
    out
  }

  def suggestions: String = {
    var s = f"Possible moves for $current: "
    for {e <- moves.values.flatten.toSet.toList.sorted}
      s += mapOutput(e._1, e._2) + " "
    s
  }

  def mapOutput(x: Int, y: Int): String = (x + 65).toChar.toString + (y + 1)

  /*
  def result(p: Vector[Player]): String = {
    val p1count = p(0).count
    val p2count = p(1).count
    val winner = if (p1count >= p2count) p(0) else p(1)
    val loser = if (winner == p(0)) p(1) else p(0)
    if (p1count != p2count) {
      f"$winner wins by ${winner.count}:${loser.count}!"
    } else {
      f"Draw. $p1count:$p2count"
    }
  }
 */
}
