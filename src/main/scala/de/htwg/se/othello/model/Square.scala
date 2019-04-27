package de.htwg.se.othello.model

case class Square(value: Int) {

  def isSet: Boolean = value > 0

  override def toString: String = {
    if (value == 2) {
      f"$value|"
    }
    else if (value == 1) {
      f"\u001B[34m$value%d\u001B[0m|"
    }
    else if (value == 0) {
      "_|"
    }
    else {
      "x|"
    }
  }
}
