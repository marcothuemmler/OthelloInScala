package de.htwg.se.othello.model

case class Board(grid: Vector[Vector[Square]]) {

  def this() = {
    this(Vector.tabulate(8, 8)((i, j) => {
      if ((i == 4 || i == 3) && i == j) Square(2)
      else if (i == 4 && j == 3 || i == 3 && j == 4) Square(1)
      else Square(0)
    }))
  }

  def isSet(col: Int, row: Int): Boolean = grid(col)(row).isSet

  def valueOf(col: Int, row: Int): Int = grid(col)(row).value

  def highlight(col: Int, row: Int): Board = flip(col, row, -1)

  def flip(col: Int, row: Int, value: Int): Board = {
    copy(grid.updated(col, grid(col).updated(row, Square(value))))
  }

  def isHighlighted: Boolean = countHighlighted > 0

  def countHighlighted: Int = grid.flatten.count(o => o.isHighlighted)

  def countAll(v1: Int, v2: Int): (Int, Int) = (count(v1), count(v2))

  def count(value: Int): Int = grid.flatten.count(o => o.value == value)

  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Board = {
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    val board = flip(current._1, current._2, value)
    if (current != end) board.flipLine((nextH, nextV), end, value)
    else board
  }

  def deHighlight: Board = {
    copy(Vector.tabulate(8, 8)((col, row) => {
      if (grid(col)(row).isHighlighted) Square(0) else grid(col)(row)
    }))
  }

  override def toString: String = {
    val top = "\n    A B C D E F G H\n    _______________"
    var board = ("\nrow  |" + ("X" * 8)) * 8 + "\n"
    for {
      col <- 0 to 7
      row <- 0 to 7
    } board = board.replaceFirst("row", f"${row + 1}")
      .replaceFirst("X", f"${grid(row)(col)}")
    top + board + "    ⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺"
  }
}
