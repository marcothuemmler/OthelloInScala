package de.htwg.se.othello

sealed trait Color
case object Black extends Color()
case object White extends Color()

case class Cell(color: Option[Color]) {


}
object  Cell {
  val BlackCell = Cell(Some(Black))
  val WhiteCell = Cell(Some(White))
  val NoCell = Cell(None)
}

