package de.htwg.se.othello.model

import scala.collection.mutable.ListBuffer

class Player(name: String, value: Int, game: Game) {

  def this(value: Int, game: Game) = this(f"Player$value", value, game)

  def setByPl(x: Int, y: Int): Boolean = game.valueOf(x, y) == this.value

  def setByOpp(x: Int, y: Int): Boolean = {
    game.valueOf(x, y) > 0 && !setByPl(x, y)
  }

  def moves: Map[(Int, Int), List[(Int, Int)]] = {
    val moves = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7
         if setByPl(i, j) && checkMoves(i,j)._2.nonEmpty) {
      moves += checkMoves(i, j)
    }
    moves.toMap
  }

  def highlight(): Unit = {
    for (v <- moves.values.flatten) {
      game.flip(v._1, v._2, -1)
    }
  }

  def set(x: Int, y: Int): Boolean = {
    val allMoves = moves
    val valid = allMoves.filter(_._2.contains((x, y)))
    if (valid.nonEmpty) {
      for (tile <- valid.keys) {
        game.flip((x, y), tile, value)
      }
      for (tile <- allMoves.values.flatten.filter(_ != (x, y))) {
        game.flip(tile._1, tile._2, 0)
      }
      return true
    }
    false
  }

  def checkMoves(x: Int, y: Int): ((Int, Int), List[(Int, Int)]) = {
    val list = new ListBuffer[(Int, Int)]
    for (i <- -1 to 1; j <- -1 to 1) {
      list += check(x, y, (i, j))
    }
    ((x, y), list.filter(_ != (-1, -1)).toList)
  }

  def check(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || !setByOpp(nX, nY)) {
      (-1, -1)
    } else {
      checkRec(nX, nY, direction)
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

  def count: Int = game.board.field.flatten.count(_.value == value)

  override def toString: String = name
}
