package de.htwg.se.othello

import scala.collection.mutable.ListBuffer

class Game {

  val board = new Board

  def setByPl(x: Int, y: Int, p: Player): Boolean = board.getValue(x,y) == p.value
  def setByOpp(x: Int, y: Int, p: Player): Boolean = board.isSet(x, y) && !setByPl(x, y, p)
  def update(): Unit = println(board)

  def moves(p: Player): Map[(Int, Int), List[(Int, Int)]] = {
    val valid = new ListBuffer[((Int, Int), List[(Int, Int)])]
    for (i <- 0 to 7; j <- 0 to 7 if board.isSet(i, j)) {
      val moves = checkMoves(i, j, p)
      if (moves._2.nonEmpty) valid += moves
    }
    valid.toMap
  }

  def flip(x: Int, y: Int, p: Player): Unit = {
    if (moves(p).exists(e => e._2.contains((x, y)))) {
      val arr = moves(p).filter(e => e._2.contains((x,y))).keys.toList
      for (i <- arr.indices) {
        flipLine((x, y), arr(i), p)
      }
      update()
    } else {
      val valid = moves(p).values.flatten.toList
      println("Invalid move, please try again. Possible moves: ")
      for (i <- valid.indices) {
        print("[" + valid(i)._1 + ", " + valid(i)._2 + "] ")
      }
      println
    }
  }

  def checkMoves(x: Int, y: Int, p: Player): ((Int, Int), List[(Int, Int)]) = {
    val list = new ListBuffer[(Int, Int)]
    if (x > 1) list += checkLeft(x, y, p)
    if (x < 6) list += checkRight(x, y, p)
    if (y < 6) list += checkDown(x, y, p)
    if (y > 1) list += checkUp(x, y, p)
    if (x > 1 && y > 1) list += checkUpLeft(x, y, p)
    if (x < 6 && y > 0) list += checkUpRight(x, y, p)
    if (x > 1 && y < 6) list += checkDownLeft(x, y, p)
    if (x < 6 && y < 6) list += checkDownRight(x, y, p)
    ((x,y), list.filter(_ != (-1,-1)).toList)
  }

  def checkUp(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x, y - 1)) {
      checkRec(x, y, p, 0, -1)
    } else (-1, -1)
  }

  def checkRight(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x + 1, y)) {
      checkRec(x, y, p, 1, 0)
    } else (-1, -1)
  }

  def checkLeft(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x - 1, y)) {
      checkRec(x, y, p, -1, 0)
    } else (-1, -1)
  }

  def checkDown(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x, y + 1)) {
      checkRec(x, y, p, 0, 1)
    } else (-1, -1)
  }

  def checkUpLeft(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x - 1, y - 1)) {
      checkRec(x, y, p, -1, -1)
    } else (-1, -1)
  }

  def checkUpRight(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x + 1, y - 1)) {
      checkRec(x, y, p, 1, -1)
    } else (-1, -1)
  }

  def checkDownLeft(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x - 1, y + 1)) {
      checkRec(x, y, p, -1, 1)
    } else (-1, -1)
  }

  def checkDownRight(x: Int, y: Int, p: Player): (Int, Int) = {
    if (setByPl(x, y, p) && board.isSet(x + 1, y + 1)) {
      checkRec(x, y, p, 1, 1)
    } else (-1, -1)
  }

  def checkRec(x: Int, y: Int, p: Player, dX: Int, dY: Int): (Int, Int) = {
    val xVal = x + dX
    val yVal = y + dY
    if ((xVal < 0 || xVal > 7) || (yVal < 0 || yVal > 7) || setByPl(xVal, yVal, p)) {
      (-1,-1)
    }
    else if (setByOpp(xVal, yVal, p)) {
      checkRec(xVal, yVal, p, dX, dY)
    }

    else {
      (xVal, yVal)
    }
  }

  def flipLine(n: (Int, Int), o: (Int, Int), p: Player): Unit = {
    //up
    if (n._1 == o._1 && n._2 < o._2) {
      for (i <- n._2 until o._2) {
        board.flip(n._1, i, p.value)
      }
    }
    // left
    if (n._1 < o._1 && n._2 == o._2) {
      for (i <- n._1 until o._1) {
        board.flip(i, n._2, p.value)
      }
    }
    // right
    if (n._1 > o._1 && n._2 == o._2) {
      for (i <- o._1 to n._1) {
        board.flip(i, n._2, p.value)
      }
    }
    // down
    if (n._1 == o._1 && n._2 > o._2) {
      for (i <- o._2 to n._2) {
        board.flip(o._1 , i, p.value)
      }
    }
    // up left
    if (n._1 < o._1 && n._2 < o._2) {
      for (i <- n._1 to o._1; j <- n._2 to o._2 if i - n._1 == j - n._2) {
        board.flip(i , j, p.value)
      }
    }
    // up right
    if (n._1 > o._1 && n._2 < o._2) {
      for (i <- o._1 to n._1; j <- n._2 to o._2 if i - o._1 == o._2 - j) {
        board.flip(i , j, p.value)
      }
    }
    // down right
    if (n._1 > o._1 && n._2 > o._2) {
      for (i <- o._1 to n._1; j <- o._2 to n._2 if i - n._1 == j - n._2) {
        board.flip(i , j, p.value)
        println(i,j)
      }
    }
    // down left
    if (n._1 < o._1 && n._2 > o._2) {
      for (i <- n._1 to o._1; j <- o._2 to n._2 if i - o._1 == o._2 - j) {
        board.flip(i , j, p.value)
        println(i,j)
      }
    }
  }

  override def toString: String = board.toString
}
