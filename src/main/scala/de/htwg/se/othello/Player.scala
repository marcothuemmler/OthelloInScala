package de.htwg.se.othello

import scala.collection.mutable.ListBuffer

case class Player(name: String, value: Int, game: Game) {

  val board: Board = game.board

  def moves(): Map[(Int, Int), List[(Int, Int)]] = {
    val valid = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7 if board.isSet(i, j)) {
      valid += checkMoves(i, j)
    }
    valid.filter(_._2.nonEmpty).toMap
  }

  def setByPl(x: Int, y: Int): Boolean = {
    board.getValue(x, y) == this.value
  }

  def setByOpp(x: Int, y: Int): Boolean = {
    board.isSet(x, y) && !setByPl(x, y)
  }

  def set(x: Int, y: Int): Unit = {
    val validMoves = moves().filter(_._2.contains((x, y)))
    if (validMoves.nonEmpty) {
      val flipSet = validMoves.keys
      for (cell <- flipSet) {
        game.flipLine((x, y), cell, value)
      }
      println(board)
    } else {
      val valid = moves().values.flatten.toSet
      println(f"Please try again. Possible moves for $name%s:")
      for (move <- valid) {
        printf(f"$move ")
      }
      println
    }
  }

  def checkMoves(x: Int, y: Int): ((Int, Int), List[(Int, Int)]) = {
    val list = new ListBuffer[(Int, Int)]
    for (i <- -1 to 1; j <- -1 to 1) {
      list += check(x, y, i, j)
    }
    ((x, y), list.filter(_ != (-1, -1)).toList)
  }

  def check(x: Int, y: Int, dX: Int, dY: Int): (Int, Int) = {
    val xVal = x + dX
    val yVal = y + dY
    if (xVal < 0 || xVal > 7 || yVal < 0 || yVal > 7) {
      (-1, -1)
    } else {
      if (setByPl(x, y) && board.isSet(xVal, yVal)) {
        checkRec(x, y, dX, dY)
      } else {
        (-1, -1)
      }
    }
  }

  def checkRec(x: Int, y: Int, dX: Int, dY: Int): (Int, Int) = {
    val xVal = x + dX
    val yVal = y + dY
    if (xVal < 0 || xVal > 7 || yVal < 0 || yVal > 7 || setByPl(xVal, yVal)) {
      (-1, -1)
    } else if (setByOpp(xVal, yVal)) {
      checkRec(xVal, yVal, dX, dY)
    } else {
      (xVal, yVal)
    }
  }
}
