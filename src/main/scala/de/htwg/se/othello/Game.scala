package de.htwg.se.othello

class Game {

  val board = new Board

  def flipLine(n: (Int, Int), o: (Int, Int), value: Int): Unit = {
    if (n._1 == o._1 && n._2 < o._2) { //up
      for (i <- n._2 until o._2) {
        board.flip(n._1, i, value)
      }
    } else if (n._1 < o._1 && n._2 == o._2) { // left
      for (i <- n._1 until o._1) {
        board.flip(i, n._2, value)
      }
    } else if (n._1 > o._1 && n._2 == o._2) { // right
      for (i <- o._1 to n._1) {
        board.flip(i, n._2, value)
      }
    } else if (n._1 == o._1 && n._2 > o._2) { // down
      for (i <- o._2 to n._2) {
        board.flip(o._1, i, value)
      }
    } else if (n._1 < o._1 && n._2 < o._2) { // up left
      for (i <- n._1 to o._1; j <- n._2 to o._2 if i - n._1 == j - n._2) {
        board.flip(i, j, value)
      }
    } else if (n._1 > o._1 && n._2 < o._2) { // up right
      for (i <- o._1 to n._1; j <- n._2 to o._2 if i - o._1 == o._2 - j) {
        board.flip(i, j, value)
      }
    } else if (n._1 > o._1 && n._2 > o._2) { // down right
      for (i <- o._1 to n._1; j <- o._2 to n._2 if i - n._1 == j - n._2) {
        board.flip(i, j, value)
      }
    } else if (n._1 < o._1 && n._2 > o._2) { // down left
      for (i <- n._1 to o._1; j <- o._2 to n._2 if i - o._1 == o._2 - j) {
        board.flip(i, j, value)
      }
    }
  }
}
