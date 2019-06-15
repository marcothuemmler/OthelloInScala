package de.htwg.se.othello.model

case class Square(value: Int) {

  def isSet: Boolean = value > 0

  def isHighlighted: Boolean = value < 0

  override def toString: String = {
    if (value == 2) "○|"
    else if (value == 1) "●|"
    else if (value == 0) "_|"
    else "x|"
  }
}
