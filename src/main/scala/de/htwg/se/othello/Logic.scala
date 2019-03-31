package de.htwg.se.othello

import scala.collection.mutable.ListBuffer

// TODO: Flesh out checks, implement diagonal, check edge cases
case class Logic(board: Board) {

  def setByPlayer(x: Int, y: Int, player: Player): Boolean = {
    board.getValue(x,y) == player.value
  }

  def setByOpponent(x: Int, y: Int, player: Player): Boolean = {
    board.isSet(x, y) && board.getValue(x,y) != player.value
  }

  def validMoves(player: Player): List[(Int, Int)] = {
    val validMoves = new ListBuffer[(Int, Int)]
    for (i <- board.field.indices) {
      for (j <- board.field.indices) {
        if (checkMoves(i, j, player).nonEmpty) {
          validMoves ++= checkMoves(i, j, player)
        }
      }
    }
    validMoves.toList
  }

  def checkMoves(x: Int, y: Int, player: Player): List[(Int, Int)] = {
    val list = new ListBuffer[(Int, Int)]
    if (checkRight(x, y, player) != (-1,-1)) list += checkRight(x, y, player)
    if (checkDown(x, y, player) != (-1,-1)) list += checkDown(x, y, player)
    if (checkLeft(x, y, player) != (-1,-1)) list += checkLeft(x, y, player)
    if (checkUp(x, y, player) != (-1,-1)) list += checkUp(x, y, player)
    list.toList
  }

  def checkUp(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x, y - 1)) {
      checkRecUp(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkRight(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x + 1, y)) {
      checkRecRight(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkLeft(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x - 1, y)) {
      checkRecLeft(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkDown(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x, y + 1)) {
      checkRecDown(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkRecUp(x: Int, y: Int, player: Player): (Int, Int) = {
    val yVal = y - 1
    if (setByOpponent(x, yVal, player)) {
      checkRecUp(x, yVal, player)
    } else if (setByPlayer(x, yVal, player)) {
      (-1,-1)
    } else {
      (x, yVal)
    }
  }

  def checkRecRight(x: Int, y: Int, player: Player): (Int, Int) = {
    val xVal = x + 1
    if (setByOpponent(xVal, y, player)) {
      checkRecRight(xVal, y, player)
    } else if (setByPlayer(xVal, y, player)) {
      (-1,-1)
    } else {
      (xVal, y)
    }
  }

  def checkRecLeft(x: Int, y: Int, player: Player): (Int, Int) = {
    val xVal = x - 1
    if (setByOpponent(xVal, y, player)) {
      checkRecLeft(xVal, y, player)
    } else if (setByPlayer(xVal, y, player)) {
      (-1,-1)
    } else {
      (xVal, y)
    }
  }

  def checkRecDown(x: Int, y: Int, player: Player): (Int, Int) = {
    val yVal = y + 1
    if (setByOpponent(x, yVal, player)) {
      checkRecDown(x, yVal, player)
    } else if (setByPlayer(x, yVal, player)) {
      (-1,-1)
    } else {
      (x, yVal)
    }
  }
}
