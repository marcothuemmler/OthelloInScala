package de.htwg.se.othello.model

case class Player(name: String, value: Int) {

  def isBot: Boolean = isInstanceOf[Bot]

  override def toString: String = this.name
}

object Player {
  def apply(value: Int) = new Player(if (value == 1) "Black" else "White", value)
}
