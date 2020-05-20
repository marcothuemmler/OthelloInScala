package de.htwg.se.othello.model

case class Player(name: String, value: Int) {

  def this(value: Int) = this(if (value == 1) "Black" else "White", value)

  def isBot: Boolean = isInstanceOf[Bot]

  override def toString: String = this.name
}