package de.htwg.se.othello

case class Player(name: String, value: Int) {

  override def toString: String = {
    f"$name%s plays as $value%d"
  }
}