package de.htwg.se.othello.model

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
