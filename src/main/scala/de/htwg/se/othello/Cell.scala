package de.htwg.se.othello

sealed trait Color
case object Black extends Color()
case object White extends Color()

case class Cell(value: Int) {
  override def toString: String = {
    if (value > 0) {
      f"$value%d|"
    } else if (value == 0) {
      "_|"
    } else {
      "x "
    }
  }
}

object  Cell {
  val BlackCell = Cell(2)
  val WhiteCell = Cell(1)
  val NoCell = Cell(0)
}
sealed trait CellState{

}

object CellState {

  case object Black extends CellState

  case object White extends CellState

  case object NoCell extends CellState


}