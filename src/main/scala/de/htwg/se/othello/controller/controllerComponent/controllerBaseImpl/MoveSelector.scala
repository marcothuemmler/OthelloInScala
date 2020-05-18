package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import boardComponent.BoardInterface

abstract class MoveSelector(controller: Controller) {

  private type Move = (Int, Option[(Int, Int)])
  val player: Int = controller.player.value
  val notPlayer: Int = controller.nextPlayer.value
  val weightedBoard: Vector[Vector[Int]] = Vector(
    Vector(99,  -8,  8,  6,  6,  8,  -8, 99),
    Vector(-8, -24, -4, -3, -3, -4, -24, -8),
    Vector(8,   -4,  7,  4,  4,  7,  -4,  8),
    Vector(6,   -3,  4,  0,  0,  4,  -3,  6),
    Vector(6,   -3,  4,  0,  0,  4,  -3,  6),
    Vector(8,   -4,  7,  4,  4,  7,  -4,  8),
    Vector(-8, -24, -4, -3, -3, -4, -24, -8),
    Vector(99,  -8,  8,  6,  6,  8,  -8, 99)
  )

  def selection: (Int, Int) = {
    val before = System.currentTimeMillis
    val res = {
      if (controller.count(1) + controller.count(2) <= 6 || controller.size != 8)
        controller.options(scala.util.Random.nextInt(controller.options.size))
      else
        search(player, 5, controller.board, None, -10000, 10000, m = true)._2.get
    }
    val after = System.currentTimeMillis
    if (after - before < 500) Thread.sleep(500 - after + before)
    res
  }

  def evaluate(b: BoardInterface): Int

  def max(x: Move, y: Move): Move = if (x._1 >= y._1) x else y

  def min(x: Move, y: Move): Move = if (x._1 <= y._1) x else (y._1, x._2)

  def sumUp(b: BoardInterface, value: Int): Int = {
    (for {
      x <- 0 until b.size
      y <- 0 until b.size if b.valueOf(x, y) == value
    } yield weightedBoard(x)(y)).sum
  }

  def search(p: Int, d: Int, n: BoardInterface, move: Option[(Int, Int)], alpha: Int, beta: Int, m: Boolean): Move = {
    if (d == 0 || n.gameOver || n.moves(p).isEmpty) (evaluate(n), move)
    else if (m) {
      n.moves(player).values.flatten.foldLeft(alpha, move) {
        case ((a, prev), next) if beta > a =>
          max((a, prev), search(notPlayer, d - 1, simulate(n, player, next), Option(next), a, beta, !m))
        case ((a, prev), _) if beta <= a => (a, prev)
      }
    } else {
      n.moves(notPlayer).values.flatten.foldLeft((beta, move)) {
        case ((b, prev), next) if b > alpha =>
          min((b, prev), search(player, d - 1, simulate(n, notPlayer, next), Option(next), alpha, b, !m))
        case ((b, prev), _) if b <= alpha => (alpha, prev)
      }
    }
  }

  def simulate(b: BoardInterface, p: Int, toSquare: (Int, Int)): BoardInterface = {
    var newBoard = b
    b.moves(p).filter(o => o._2.contains(toSquare)).keys.foreach(fromSquare =>
      newBoard = b.flipLine(fromSquare, toSquare, p))
    newBoard
  }
}

class HardBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    if (b.gameOver) b.count(player).compare(b.count(notPlayer)) * 5000
    else {
      (for {
        x <- 0 until b.size
        y <- 0 until b.size if b.valueOf(x, y) > 0
      } yield b.valueOf(x, y) match {
        case `player` => weightedBoard(x)(y)
        case `notPlayer` => -weightedBoard(x)(y)
      }).sum - b.moves(notPlayer).values.flatten.toSet.size * 10
    }
  }
}

class MediumBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    if (b.gameOver) b.count(player).compare(b.count(notPlayer)) * 5000
    else sumUp(b, player)
  }
}

class EasyBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    if (b.gameOver) -b.count(player).compare(b.count(notPlayer)) * 5000
    else sumUp(b, notPlayer)
  }
}
