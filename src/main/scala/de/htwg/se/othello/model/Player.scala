package de.htwg.se.othello.model

import scala.collection.mutable.ListBuffer

class Player(name: String, value: Int, game: Game) {

  val board: Board = game.board

  def setByOpp(x: Int, y: Int): Boolean = {
    board.field(x)(y).value > 0 && board.field(x)(y).value != this.value
  }

  def moves(): Map[(Int, Int), List[(Int, Int)]] = {
    val moves = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7 if board.field(i)(j).value == value) {
      moves += checkMoves(i, j)
    }
    moves.toMap
  }

  def highlight(): Unit = {
    for (v <- moves().values.flatten) {
      game.flip(v._1, v._2, -1)
    }
    game.update()
  }

  def set(x: Int, y: Int): Unit = {
    val allMoves = moves()
    val valid = allMoves.filter(_._2.contains((x, y)))
    if (valid.nonEmpty) {
      for (tile <- valid.keys) {
        game.flip((x, y), tile, value)
      }
      for (tile <- allMoves.values.flatten.filter(_ != (x,y))) {
        game.flip(tile._1, tile._2, 0)
      }
      game.update()
    } else {
      println(f"Please try again. Possible moves for $name%s:")
      val possible = allMoves.values.flatten.toSet
      for (move <- possible) {
        printf(f"$move ")
      }
      println
    }
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
      checkRec(x, y, direction)
    }
  }

  def checkRec(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7
      || board.field(nX)(nY).value == value) {
      (-1, -1)
    } else if (setByOpp(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (nX, nY)
    }
  }
}
