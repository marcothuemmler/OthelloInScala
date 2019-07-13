package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface

import scala.util.{Random, Try}

abstract class MoveSelector(controller: Controller) {

  private type Move = (Int, Option[(Int, Int)])
  val player: Player = controller.player
  val betaP: Player = controller.nextPlayer
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

  def select = Try {
    val before = System.currentTimeMillis
    val res = {
      if (controller.count(1) + controller.count(2) <= 6 || controller.size != 8)
        controller.options(Random.nextInt(controller.options.size))
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

  def search(p: Player, d: Int, n: BoardInterface, move: Option[(Int, Int)], alpha: Int, beta: Int, m: Boolean): Move = {
    if (d == 0 || n.gameOver || n.moves(p.value).isEmpty) (evaluate(n), move)
    else if (m) {
      n.moves(player.value).values.flatten.toSet.foldLeft(alpha, move) {
        case ((a, prev), next) if beta > a =>
          val newBoard = simulate(n, player, next)
          max((a, prev), search(betaP, d - 1, newBoard, Option(next), a, beta, !m))
        case ((a, prev), _) if beta <= a => (a, prev)
      }
    } else {
      n.moves(betaP.value).values.flatten.toSet.foldLeft((beta, move)) {
        case ((b, prev), next) if b > alpha =>
          val newBoard = simulate(n, betaP, next)
          min((b, prev), search(player, d - 1, newBoard, Option(next), alpha, b, !m))
        case ((b, prev), _) if b <= alpha => (alpha, prev)
      }
    }
  }

  def simulate(b: BoardInterface, p: Player, toSquare: (Int, Int)): BoardInterface = {
    var newBoard = b
    for {
      fromSquare <- b.moves(p.value).filter(o => o._2.contains(toSquare)).keys
    } newBoard = b.flipLine(fromSquare, toSquare, p.value)
    newBoard
  }
}

// Combination of positional strategy, absolute strategy and mobility
class HardBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    if (b.gameOver) b.count(player.value).compare(b.count(betaP.value)) * 5000
    else {
      (for {
        x <- 0 until b.size
        y <- 0 until b.size
      } yield b.valueOf(x, y) match {
        case player.value => weightedBoard(x)(y)
        case betaP.value => -weightedBoard(x)(y)
        case _ => 0
      }).sum - b.moves(betaP.value).values.flatten.toSet.size * 10
    }
  }
}

// Positional strategy and absolute strategy
class MediumBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    if (b.gameOver) b.count(player.value).compare(b.count(betaP.value)) * 5000
    else {
      (for {
        x <- 0 until b.size
        y <- 0 until b.size
        if b.valueOf(x, y) == player.value
      } yield weightedBoard(x)(y)).sum
    }
  }
}

// Positional strategy reversed
class EasyBot(controller: Controller) extends MoveSelector(controller) {
  def evaluate(b: BoardInterface): Int = {
    (for {
      x <- 0 until b.size
      y <- 0 until b.size
      if b.valueOf(x, y) == player.value
    } yield -weightedBoard(x)(y)).sum
  }
}
