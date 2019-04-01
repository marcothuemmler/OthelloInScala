package de.htwg.se.othello

case class Cell(value: Int) {
  def isSet: Boolean = value != 0
  override def toString: String = if (value != 0) f"$value%d|" else "_|"
}
