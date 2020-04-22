package de.htwg.se.othello.model

class Bot(name: String, value: Int) extends Player(name, value) {
  def this(value: Int) = this(if (value == 1) "Black" else "White", value)
}
