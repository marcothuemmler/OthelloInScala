package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.htwg.se.othello.model.boardComponent.BoardInterface
import play.api.libs.json.{JsObject, Json}

import scala.annotation.tailrec

case class Board(grid: Vector[Vector[Square]]) extends BoardInterface {

  lazy val gameOver: Boolean = moves(1).isEmpty && moves(2).isEmpty && nonEmpty
  val nonEmpty: Boolean = grid.flatten.exists(o => o.isSet)
  val size: Int = grid.size

  @Inject
  def this(@Assisted size: Int) = this(Vector.fill(size, size)(Square(0)))

  def moves(implicit value: Int): Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      col <- grid.indices
      row <- grid.indices if valueOf(col, row) == value
    } yield getMoves(col, row)).filter(_._2.nonEmpty).toMap
  }

  def getMoves(col: Int, row: Int)(implicit value: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((col, row), (for {
      x <- -1 to 1
      y <- -1 to 1
      (nX, nY) = (col + x, row + y)
      if isWithinBounds(nX, nY) && setByOpponent(nX, nY)
      checkDirection = checkRec((x, y))(_: Int, _: Int)
    } yield checkDirection(nX, nY)).flatten)
  }

  def isWithinBounds(col: Int, row: Int): Boolean = {
    col >= 0 && col < size && row >= 0 && row < size
  }

  @tailrec
  final def checkRec(direction: (Int, Int))(x: Int, y: Int)(implicit value: Int): Option[(Int, Int)] = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (!isWithinBounds(nX, nY) || valueOf(nX, nY) == value) None
    else if (setByOpponent(nX, nY)) checkRec(direction)(nX, nY)
    else Some(nX, nY)
  }

  @tailrec
  final def flipLine(curr: (Int, Int), end: (Int, Int))(implicit value: Int): Board = {
    val (col, row) = curr
    val board = copy(grid.updated(col, grid(col).updated(row, Square(value))))
    val next = (col - col.compare(end._1), row - row.compare(end._2))
    if (curr != end) board.flipLine(next, end)
    else board
  }

  def changeHighlight(implicit value: Int): Board = {
    if (grid.flatten.contains(Square(-1))) deHighlight else highlight(value)
  }

  def deHighlight: Board = copy(Vector.tabulate(size, size)((col, row) =>
    if (grid(col)(row).isHighlighted) Square(0) else grid(col)(row)
  ))

  def highlight(implicit value: Int): Board = {
    copy(Vector.tabulate(size, size)((col, row) =>
      if (moves(value).values.flatten.toSet.contains((col, row))) Square(-1)
      else grid(col)(row)
    ))
  }

  def setByOpponent(x: Int, y: Int)(implicit value: Int): Boolean = {
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

  def toHtml:String = "<p  style=\"font-family:'Lucida Console', monospace\"> " + toString.replace("\n","<br>").replace("  ","&nbsp&nbsp") +"</p>"

  def toJson: JsObject = Json.obj(
    "size" -> size,
    "squares" -> Json.toJson(
      for {
        row <- 0 until size
        col <- 0 until size
      } yield Json.obj(
        "value" -> valueOf(row, col),
        "row" -> row,
        "col" -> col
      )
    )
  )
}
