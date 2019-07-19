package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.htwg.se.othello.model.boardComponent.BoardInterface

import scala.annotation.tailrec

case class Board(grid: Vector[Vector[Square]]) extends BoardInterface {

  lazy val gameOver: Boolean = moves(1).isEmpty && moves(2).isEmpty && isSet
  val isSet: Boolean = grid.flatten.exists(o => o.isSet)
  val size: Int = grid.size

  @Inject
  def this(@Assisted size: Int) = this(Vector.fill(size, size)(Square(0)))

  def moves(value: Int): Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      col <- grid.indices
      row <- grid.indices if valueOf(col, row) == value
    } yield getMoves(value, col, row)).filter(o => o._2.nonEmpty).toMap
  }

  def getMoves(value: Int, col: Int, row: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((col, row), (for {
      x <- -1 to 1
      y <- -1 to 1
      (nX, nY) = (col + x, row + y)
      if nX >= 0 && nX < size && nY >= 0 && nY < size && setByOpp(value, nX, nY)
    } yield checkRec(value, nX, nY, (x, y))).filter(o => o.isDefined).flatten)
  }

  @tailrec
  final def checkRec(value: Int, x: Int, y: Int, direction: (Int, Int)): Option[(Int, Int)] = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX < 0 || nX >= size || nY < 0 || nY >= size || valueOf(nX, nY) == value) None
    else if (setByOpp(value, nX, nY)) checkRec(value, nX, nY, direction)
    else Some(nX, nY)
  }

  @tailrec
  final def flipLine(curr: (Int, Int), end: (Int, Int), value: Int): Board = {
    val (col, row) = curr
    val board = copy(grid.updated(col, grid(col).updated(row, Square(value))))
    val next = (col - col.compare(end._1), row - row.compare(end._2))
    if (curr != end) board.flipLine(next, end, value)
    else board
  }

  def changeHighlight(value: Int): Board = {
    if (grid.flatten.contains(Square(-1))) deHighlight else highlight(value)
  }

  def deHighlight: Board = copy(Vector.tabulate(size, size)((col, row) =>
    if (grid(col)(row).isHighlighted) Square(0) else grid(col)(row)
  ))

  def highlight(value: Int): Board = {
    copy(Vector.tabulate(size, size)((col, row) =>
      if (moves(value).values.flatten.toSet.contains((col, row))) Square(-1)
      else grid(col)(row)
    ))
  }

  def setByOpp(value: Int, x: Int, y: Int): Boolean = {
    grid(x)(y).isSet && valueOf(x, y) != value
  }

  def valueOf(col: Int, row: Int): Int = grid(col)(row).value

  def count(value: Int): Int = grid.flatten.count(o => o.value == value)

  override def toString: String = {
    val cols = grid.indices.map(i => (i + 65).toChar).mkString(" ")
    val top = s"\n    $cols\n    " + "_" * (size * 2 - 1)
    var board = ("\nrow" + ("X" * size)) * size + "\n"
    for {
      col <- grid.indices
      row <- grid.indices
    } board = board.replaceFirst(
      "row", f"${row + 1}" + (if (row + 1 > 9) " |" else "  |")
    ).replaceFirst("X", f"${grid(row)(col)}")
    f"$top$board    " + "⎺" * (size * 2 - 1)
  }
}
