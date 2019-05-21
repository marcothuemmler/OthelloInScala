package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}

import scala.util.Try

class MoveSelector(controller: Controller) {
  val board: Board = controller.board
  val alphaPlayer: Player = controller.player
  val betaPlayer: Player = {
    if (alphaPlayer == controller.players(0)) controller.players(1)
    else controller.players(0)
  }

  def options: List[(Int, Int)] = controller.options

  def select = Try(options(scala.util.Random.nextInt(options.size)))

  def evaluate: Int = {
    (for {
      x <- 0 to 7
      y <- 0 to 7
      if board.setBy(alphaPlayer.value, x, y)
    } yield weightedBoard(x)(y)).sum
  }

  val weightedBoard: Vector[Vector[Int]] = Vector(
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

object MoveSelector {
  def main(args: Array[String]): Unit = {
    val controller = new Controller
    val moveSelector = new MoveSelector(controller)
    println(moveSelector.evaluate)
  }
}
