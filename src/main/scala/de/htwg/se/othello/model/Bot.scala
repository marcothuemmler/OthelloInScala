package de.htwg.se.othello.model

class Bot(name: String, value: Int) extends Player(name, value)

object Bot {
  def apply(value: Int) = new Bot(if (value == 1) "Black" else "White", value)
}
