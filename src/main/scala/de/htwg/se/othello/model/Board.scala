package de.htwg.se.othello.model

import scala.annotation.tailrec

case class Board(grid: Vector[Vector[Square]]) {

  val size: Int = grid.size

  def this() = this(Vector.fill(8, 8)(Square(0)))

  def this(size: Int) = this(Vector.fill(size, size)(Square(0)))

  def moves(value: Int): Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      col <- grid.indices
      row <- grid.indices if setBy(value, col, row)
    } yield getMoves(value, col, row)).filter(o => o._2.nonEmpty).toMap
  }

  def getMoves(value: Int, col: Int, row: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((col, row), (for {
      x <- -1 to 1
      y <- -1 to 1
      (nX, nY) = (col + x, row + y)
      if (grid.indices contains nX) && (grid.indices contains nY) && setByOpp(value, nX, nY)
    } yield checkRec(value, nX, nY, (x, y))).filter(o => o != (-1, -1)))
  }

  @tailrec
  final def checkRec(value: Int, x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX < 0 || nX >= grid.size || nY < 0 || nY >= grid.size || setBy(value, nX, nY)) (-1, -1)
    else if (setByOpp(value, nX, nY)) checkRec(value, nX, nY, direction)
    else (nX, nY)
  }

  def flip(col: Int, row: Int, value: Int): Board = {
    copy(grid.updated(col, grid(col).updated(row, Square(value))))
  }

  @tailrec
  final def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Board = {
    val board = flip(current._1, current._2, value)
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    if (current != end) board.flipLine((nextH, nextV), end, value)
    else board
  }

  def deHighlight: Board = {
    copy(Vector.tabulate(grid.size, grid.size)((col, row) => {
      if (grid(col)(row).isHighlighted) Square(0) else grid(col)(row)
    }))
  }

  def highlight(value: Int): Board = {
    copy(Vector.tabulate(grid.size, grid.size)((col, row) => {
      if (moves(value).values.flatten.toSet.contains((col, row))) Square(-1)
      else grid(col)(row)
    }))
  }

  def isSet(col: Int, row: Int): Boolean = grid(col)(row).isSet

  def setBy(value: Int, x: Int, y: Int): Boolean = valueOf(x, y) == value

  def setByOpp(value: Int, x: Int, y: Int): Boolean = {
    isSet(x, y) && !setBy(value, x, y)
  }

  def valueOf(col: Int, row: Int): Int = grid(col)(row).value

  def isHighlighted: Boolean = grid.flatten.count(o => o.isHighlighted) > 0

  def count(value: Int): Int = grid.flatten.count(o => o.value == value)

  def gameOver: Boolean = moves(1).isEmpty && moves(2).isEmpty

  def isSet: Boolean = count(1) + count(2) > 0

  def score: String = {
    val (win, lose) = (count(1) max count(2), count(1) min count(2))
    val winner = if (win == count(1)) "Black" else "White"
    if (win != lose) f"$winner wins by $win:$lose!" else f"Draw. $win:$lose"
  }

  override def toString: String = {
    val cols = (for { i <- grid.indices } yield (i + 65).toChar).mkString(" ")
    val top = "\n    " + cols + "\n    " + "_" * (grid.size * 2 - 1)
    var board = ("\nrow" + ("X" * grid.size)) * grid.size + "\n"
    for {
      col <- grid.indices
      row <- grid.indices
    } board = board.replaceFirst(
      "row",
      f"${row + 1}" + (if (row + 1 > 9) " |" else "  |")
    )
      .replaceFirst("X", f"${grid(row)(col)}")
    top + board + "    " + "⎺" * (grid.size * 2 - 1) +
      (if (gameOver && isSet) "\n" + score else "")
  }
}
