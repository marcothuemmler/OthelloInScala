package de.htwg.se.othello.model

import scala.util.Random

class Bot(name: String, value: Int, game: Game) extends Player(name, value, game) {

  def getMove(map: Map[(Int, Int), List[(Int, Int)]]): (Int, Int) = {
    val tile = map.toList(Random.nextInt(map.keySet.size))
    tile._2(Random.nextInt(tile._2.size))
  }



}
