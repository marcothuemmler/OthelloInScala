package de.htwg.se.othello.model

case class Game(board: Board) {

  def flip(x: Int, y: Int, newVal: Int): Unit = board.field(x)(y) = Cell(newVal)

  def flipLine(n: (Int, Int), o: (Int, Int), value: Int): Unit = {
    val x = if (n._1 < o._1) 1 else -1
    val y = if (n._2 < o._2) 1 else -1
    if (n._1 == o._1 && n._2 != o._2) { // vertical
      for (i <- n._2 until o._2 by y) {
        flip(n._1, i, value)
      }
    } else if (n._1 != o._1 && n._2 == o._2) { // flip
      for (i <- n._1 until o._1 by x) {
        flip(i, n._2, value)
      }
    } else if (x == y) { // up-left || down-right
      for (i <- n._1 to o._1 by x; j <- n._2 to o._2 by y
           if i - n._1 == j - n._2) {
        flip(i, j, value)
      }
    } else { // up-right || down-left
      for (i <- n._1 to o._1 by x; j <- n._2 to o._2 by y
           if i - n._1 == n._2 - j) {
        flip(i, j, value)
      }
    }
  }

  def update(): Unit = println(board)

  def valueOf(x: Int, y: Int): Int = board.field(x)(y).value
}
