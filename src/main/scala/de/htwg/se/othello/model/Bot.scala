package de.htwg.se.othello.model

import scala.util.Random

class Bot(name: String, value: Int, game: Game) extends Player(name, value, game) {

  def this(value: Int, game: Game) = this(f"Bot$value", value, game)

  def getMove: (Int, Int) = {
    val tile = moves.toList(Random.nextInt(moves.keySet.size))
    tile._2(Random.nextInt(tile._2.size))
  }

  val pruningBoard: Vector[Vector[Int]] = Vector(
    Vector(99,  -8,  8,  6,     6,  8,  -8, 99),
    Vector(-8, -24, -4, -3     -3, -4, -24, -8),
    Vector( 8,  -4,  7,  4,     4,  7,  -4,  8),
    Vector( 6,  -3,  4,  0,     0,  4,  -3,  6),

    Vector( 6,  -3,  4,  0,     0,  4,  -3,  6),
    Vector( 8,  -4,  7,  4,     4,  7,  -4,  8),
    Vector(-8, -24, -4, -3,    -3, -4, -24, -8),
    Vector(99,  -8,  8,  6,     6,  8,  -8, 99)
  )
}
