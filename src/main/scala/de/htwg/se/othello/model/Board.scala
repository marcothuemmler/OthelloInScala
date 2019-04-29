package de.htwg.se.othello.model

case class Board(grid: Vector[Vector[Square]]) {

  def this() = {
    this(Vector.tabulate(8, 8)((i, j) => {
      if ((i == 4 || i == 3) && i == j) Square(2)
      else if (i == 4 && j == 3 || i == 3 && j == 4) Square(1)
      else Square(0)
    }))
  }

  def valueOf(x: Int, y: Int): Int = grid(x)(y).value

  def isSet(x: Int, y: Int): Boolean = grid(x)(y).isSet

  def isHighlighted(x: Int, y: Int): Boolean = grid(x)(y).isHighlighted

  def countHighlighted: Int = grid.flatten.count(o => o.isHighlighted)

  def countAll(v1: Int, v2: Int): (Int, Int) = (count(v1), count(v2))

  def count(value: Int): Int = grid.flatten.count(o => o.value == value)

  def highlight(square: (Int, Int)): Board = flip(square, -1)

  def deHighlight(square: (Int, Int)): Board = flip(square, 0)

  def flip(sq: (Int, Int), value: Int): Board = {
    copy(grid.updated(sq._1, grid(sq._1).updated(sq._2, Square(value))))
  }

  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Board = {
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    val newBoard = flip(current, value)
    if (current != end) {
      newBoard.flipLine((nextH, nextV), end, value)
    } else newBoard
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
