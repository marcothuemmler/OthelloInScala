package de.htwg.se.othello.model.boardComponent.boardBaseImpl

case class Square(value: Int) {

  val isSet: Boolean = value > 0
  val isHighlighted: Boolean = value < 0

  override def toString: String = {
    if (value == 2) "○|"
    else if (value == 1) "●|"
    else if (value == 0) "_|"
    else "x|"
  }
}
