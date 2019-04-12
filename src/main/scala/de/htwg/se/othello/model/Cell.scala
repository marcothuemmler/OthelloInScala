package de.htwg.se.othello.model

case class Cell(value: Int) {
  override def toString: String = {
    if (value == 2) f"$value%d|"
    else if (value == 1) "\u001B[34m" + f"$value%d\u001B[0m|"
    else if (value == 0) "_|"
    else "x|"
  }
}
