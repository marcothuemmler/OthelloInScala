package de.htwg.se.othello

import scala.collection.mutable.ListBuffer

class Game {

  val board = new Board
  def update(): Unit = println(board)

  def setByPl(x: Int, y: Int, p: Player): Boolean = {
    board.getValue(x,y) == p.value
  }

  def setByOpp(x: Int, y: Int, p: Player): Boolean = {
    board.isSet(x, y) && !setByPl(x, y, p)
  }

  def moves(p: Player): Map[(Int, Int), List[(Int, Int)]] = {
    val valid = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7 if board.isSet(i, j)) {
      valid += checkMoves(i, j, p)
    }
    valid.filter(_._2.nonEmpty).toMap
  }

  def flip(x: Int, y: Int, p: Player): Unit = {
    val validMoves = moves(p).filter(_._2.contains((x, y)))
    if (validMoves.nonEmpty) {
      val flipSet = validMoves.keys
      for (cell <- flipSet) {
        flipLine((x, y), cell, p)
      }
      this.update()
    } else {
      val valid = moves(p).values.flatten.toSet
      println(f"Please try again. Possible moves for ${p.name}%s:")
      for (move <- valid) {
        printf(f"$move ")
      }
      println
    }
  }

  def checkMoves(x: Int, y: Int, p: Player): ((Int, Int), List[(Int, Int)]) = {
    val list = new ListBuffer[(Int, Int)]
    for (i <- -1 to 1; j <- -1 to 1) {
      list += check(x, y, p, i, j)
    }
    ((x,y), list.filter(_ != (-1, -1)).toList)
  }

  def check(x: Int, y: Int, p: Player, dX: Int, dY: Int): (Int, Int) = {
    val xVal = x + dX
    val yVal = y + dY
    if (xVal < 0 || xVal > 7 || yVal < 0 || yVal > 7) {
      (-1, -1)
    } else {
      if (setByPl(x, y, p) && board.isSet(xVal, yVal)) {
        checkRec(x, y, p, dX, dY)
      } else {
        (-1, -1)
      }
    }
  }

  def checkRec(x: Int, y: Int, p: Player, dX: Int, dY: Int): (Int, Int) = {
    val xVal = x + dX
    val yVal = y + dY
    if (xVal < 0 || xVal > 7 || yVal < 0 || yVal > 7 || setByPl(xVal, yVal, p)) {
      (-1,-1)
    } else if (setByOpp(xVal, yVal, p)) {
      checkRec(xVal, yVal, p, dX, dY)
    } else {
      (xVal, yVal)
    }
  }

  def flipLine(n: (Int, Int), o: (Int, Int), p: Player): Unit = {
    if (n._1 == o._1 && n._2 < o._2) { //up
      for (i <- n._2 until o._2) {
        board.flip(n._1, i, p.value)
      }
    } else if (n._1 < o._1 && n._2 == o._2) { // left
      for (i <- n._1 until o._1) {
        board.flip(i, n._2, p.value)
      }
    } else if (n._1 > o._1 && n._2 == o._2) { // right
      for (i <- o._1 to n._1) {
        board.flip(i, n._2, p.value)
      }
    } else if (n._1 == o._1 && n._2 > o._2) { // down
      for (i <- o._2 to n._2) {
        board.flip(o._1 , i, p.value)
      }
    } else if (n._1 < o._1 && n._2 < o._2) { // up left
      for (i <- n._1 to o._1; j <- n._2 to o._2 if i - n._1 == j - n._2) {
        board.flip(i , j, p.value)
      }
    } else if (n._1 > o._1 && n._2 < o._2) { // up right
      for (i <- o._1 to n._1; j <- n._2 to o._2 if i - o._1 == o._2 - j) {
        board.flip(i , j, p.value)
      }
    } else if (n._1 > o._1 && n._2 > o._2) { // down right
      for (i <- o._1 to n._1; j <- o._2 to n._2 if i - n._1 == j - n._2) {
        board.flip(i , j, p.value)
      }
    } else if (n._1 < o._1 && n._2 > o._2) { // down left
      for (i <- n._1 to o._1; j <- o._2 to n._2 if i - o._1 == o._2 - j) {
        board.flip(i , j, p.value)
      }
    }
  }
}
