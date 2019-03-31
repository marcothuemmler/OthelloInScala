package de.htwg.se.othello

import scala.collection.mutable.ListBuffer

// TODO: Flesh out checks, implement diagonal, check edge cases (borders etc)
case class Logic(board: Board) {

  def setByPlayer(x: Int, y: Int, p: Player): Boolean = {
    board.getValue(x,y) == p.value
  }

  def setByOpponent(x: Int, y: Int, p: Player): Boolean = {
    board.isSet(x, y) && !setByPlayer(x, y, p)
  }

  def validMoves(p: Player): Map[(Int, Int), List[(Int, Int)]] = {
    val validMoves = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7 if board.isSet(i, j)) {
      val moves = checkMoves(i, j, p)
      if (moves._2.nonEmpty) {
        validMoves += moves
      }
    }
    validMoves.toMap
  }

  def checkMoves(x: Int, y: Int, p: Player): ((Int, Int), List[(Int, Int)]) = {
    val list = new ListBuffer[(Int, Int)]
    if (checkRight(x, y, p) != (-1,-1)) list += checkRight(x, y, p)
    if (checkDown(x, y, p) != (-1,-1)) list += checkDown(x, y, p)
    if (checkLeft(x, y, p) != (-1,-1)) list += checkLeft(x, y, p)
    if (checkUp(x, y, p) != (-1,-1)) list += checkUp(x, y, p)
    ((x,y), list.toList)
  }

  def checkUp(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPlayer(x, y, p) && board.isSet(x, y - 1)) {
      checkRecUp(x, y, p)
    } else {
      (-1, -1)
    }
  }

  def checkRight(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPlayer(x, y, p) && board.isSet(x + 1, y)) {
      checkRecRight(x, y, p)
    } else {
      (-1, -1)
    }
  }

  def checkLeft(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPlayer(x, y, p) && board.isSet(x - 1, y)) {
      checkRecLeft(x, y, p)
    } else {
      (-1, -1)
    }
  }

  def checkDown(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPlayer(x, y, p) && board.isSet(x, y + 1)) {
      checkRecDown(x, y, p)
    } else {
      (-1, -1)
    }
  }

  def checkRecUp(x: Int, y: Int, p: Player): (Int, Int) = {
    val yVal = y - 1
    if (setByOpponent(x, yVal, p)) {
      checkRecUp(x, yVal, p)
    } else if (setByPlayer(x, yVal, p)) {
      (-1,-1)
    } else {
      (x, yVal)
    }
  }

  def checkRecRight(x: Int, y: Int, p: Player): (Int, Int) = {
    val xVal = x + 1
    if (setByOpponent(xVal, y, p)) {
      checkRecRight(xVal, y, p)
    } else if (setByPlayer(xVal, y, p)) {
      (-1,-1)
    } else {
      (xVal, y)
    }
  }

  def checkRecLeft(x: Int, y: Int, p: Player): (Int, Int) = {
    val xVal = x - 1
    if (setByOpponent(xVal, y, p)) {
      checkRecLeft(xVal, y, p)
    } else if (setByPlayer(xVal, y, p)) {
      (-1,-1)
    } else {
      (xVal, y)
    }
  }

  def checkRecDown(x: Int, y: Int, p: Player): (Int, Int) = {
    val yVal = y + 1
    if (setByOpponent(x, yVal, p)) {
      checkRecDown(x, yVal, p)
    } else if (setByPlayer(x, yVal, p)) {
      (-1,-1)
    } else {
      (x, yVal)
    }
  }
}
