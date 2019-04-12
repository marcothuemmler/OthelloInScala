package de.htwg.se.othello.model

import scala.util.Random

class Bot(name: String, value: Int, game: Game) extends Player(name, value, game) {

  def this(value: Int, game: Game) {
    this(s"Bot$value", value, game)
  }

  def getMove: (Int, Int) = {
    val tile = moves.toList(Random.nextInt(moves.keySet.size))
    tile._2(Random.nextInt(tile._2.size))
  }
}
