package de.htwg.se.othello.model

case class Player(name: String, value: Int) {
  def this(value: Int) = this(f"Player$value", value)

  override def toString: String = this.name
}
