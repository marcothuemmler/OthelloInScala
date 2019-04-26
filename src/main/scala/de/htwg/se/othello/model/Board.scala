package de.htwg.se.othello.model

case class Board(grid: Array[Array[Square]]) {

  def this() = {
    this(Array.tabulate(8, 8)((i, j) => {
      if ((i == 4 || i == 3) && i == j) {
        Square(2)
      }
      else if (i == 4 && j == 3 || i == 3 && j == 4) {
        Square(1)
      }
      else {
        Square(0)
      }
    }))
  }

  /* def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Board = {
   val nextH = current._1 - current._1.compare(end._1)
   val nextV = current._2 - current._2.compare(end._2)
   val newBoard = flip(current._1, current._2, value)
   if (current != end) {
     newBoard.flipLine((nextH, nextV), end, value)
   } else {
     newBoard
   }
  } */

  /*def flip(x: Int, y: Int, newVal: Int): Board = {
    copy(grid.updated(x, grid(x).updated(y, Square(newVal))))
  }*/

  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Unit = {
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    flip(current, value)
    if (current != end) {
      flipLine((nextH, nextV), end, value)
    }
  }

  def flip(square: (Int, Int), newVal: Int): Unit = {
    val (x, y) = square
    grid(x)(y) = Square(newVal)
  }

  override def toString: String = {
    val top = "\n    A B C D E F G H\n    _______________"
    var board = ("\nrow  |" + ("X" * 8)) * 8 + "\n"
    for {
      row <- 0 to 7
      column <- 0 to 7
    } board = board.replaceFirst("row", (column + 1).toString)
      .replaceFirst("X", grid(column)(row).toString)
    top + board + "    ⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺"
  }
}
