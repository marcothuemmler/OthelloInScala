package de.htwg.se.othello.controller
import de.htwg.se.othello.model.{Board, Player}

import scala.util.Try

class MoveSelector(controller: Controller) {

  private type Move = (Int, Option[(Int, Int)])
  val player: Player = controller.player
  val board: Board = controller.board
  val betaP: Player = if (player.value == 1) new Player(2) else new Player(1)

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

  // var maxdepth = 0
  def select(depth: Int) = Try {
    // maxdepth = depth
    val result = search(depth, board, None, -1000, 1000, Max)
    /*val test = */ result._2.getOrElse(controller.options.head)
    // println("\nthis has been selected!: " + result._1 +", " + result._2.get)
    // test
  }

  def max(x: Move, y: Move): Move = if (x._1 >= y._1) x else y

  def min(x: Move, y: Move): Move = if (x._1 <= y._1) x else (y._1, x._2)

  def search(d: Int, n: Board, move: Option[(Int, Int)], alpha: Int, beta: Int, m: MinMax): Move = {
    // val depth = (for { _ <- d to maxdepth } yield "").mkString("    ")
    // if (d > 0) println("\n" + depth + d + "   " + move + "   " + alpha + "" + "   " + beta)
    if (d == 0 || n.gameOver) {
      /*val eval = */(evaluate(n), move)
      // println(depth + d + "   " + eval)
      // eval
    }
    else if (m == Max) {
      n.moves(player.value).values.flatten.toSet.foldLeft(alpha, move) {
        case ((a, prev), next) => if (beta > alpha) {
          val newBoard = simulate(n, player, next)
          max((a, prev), search(d - 1, newBoard, Option(next), a, beta, Min))
        } else (alpha, prev) // pruning
      }
    } else {
      n.moves(betaP.value).values.flatten.toSet.foldLeft((beta, move)) {
        case ((b, prev), next) => if (beta > alpha) {
          val newBoard = simulate(n, betaP, next)
          min((b, prev), search(d - 1, newBoard, Option(next), alpha, b, Max))
        } else (beta, prev) // pruning
      }
    }
  }

  def simulate(b: Board, p: Player, toSquare: (Int, Int)): Board = {
    val legal = b.moves(p.value).filter(o => o._2.contains(toSquare))
    var newBoard = b
    for { fromSquare <- legal.keys }
      newBoard = b.flipLine(fromSquare, toSquare, p.value)
    newBoard
  }

  def evaluate(b: Board): Int = {
    (for {
      x <- b.grid.indices
      y <- b.grid.indices
      if b.setBy(player.value, x, y)
    } yield weightedBoard(x)(y)).sum
  }

  trait MinMax

  object Min extends MinMax

  object Max extends MinMax
}
