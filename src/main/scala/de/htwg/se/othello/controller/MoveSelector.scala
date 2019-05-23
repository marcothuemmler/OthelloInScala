package de.htwg.se.othello.controller

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.model.{Board, Player}

class MoveSelector(p: Player) {
  type Move = (Int, Option[(Int, Int)])
  val alphaP: Player = p
  val betaP: Player = if (alphaP.value == 1) new Player(2) else new Player(1)

  def search(board: Board, player: Player, depth: Int): (Int, Int) = {
    alphaBeta(depth, board, None, player, -100000, 100000, Max)._2.getOrElse(board.moves(player.value).values.flatten.toList(0))
  }

  def alphaBeta(depth: Int, node: Board, moveChoice: Option[(Int, Int)], player: Player, alpha: Int, beta: Int, m: MinMax): Move = {
    //Thread.sleep(500)
    if (depth == 0 || node.gameOver) (evaluate(node), moveChoice)
    else if (m == Max) {
      node.moves(player.value).values.flatten.toSet.toStream.
        takeWhile(_ => beta > alpha).foldLeft((alpha, moveChoice)) {
        case ((alpha, moveChoice), move) =>
          val legal = node.moves(player.value).filter(o => o._2.contains(move))
          var test = node
          for {
            fromSquare <- legal.keys
          } test = node.flipLine(fromSquare, move, player.value)
          val res = max((alpha, moveChoice), alphaBeta(depth - 1, test, Option(move), betaP, alpha, beta, Min))
          // println("alpha: " + res + "  " + player)
          res
      }
    } else {
      node.moves(player.value).values.flatten.toSet.toStream.
        takeWhile(_ => beta > alpha).foldLeft((beta, moveChoice)) {
        case ((beta, _), move) =>
          val legal = node.moves(player.value).filter(o => o._2.contains(move))
          var test = node
          for {
            fromSquare <- legal.keys
          } test = node.flipLine(fromSquare, moveChoice.get, player.value)
          val res = min((beta, moveChoice), alphaBeta(depth - 1, test, Option(move), alphaP, alpha, beta, Max))
          // println("beta: " + res + "  " + player)
          res
      }
    }
  }

  /*def evaluate(board: Board, pl: Player): Int = {
    val bP = if (pl.value == 1) new Player(2) else new Player(1)
    val aDisks = board.count(pl.value)
    val bDisks = board.count(bP.value)
    if (board.gameOver) aDisks - bDisks
    else {
      val aCorners = board.corners(pl.value)
      val bCorners = board.corners(bP.value)
      aDisks - bDisks + aCorners - bCorners
    }
  }*/

  val weightedBoard: Vector[Vector[Int]] = Vector(
    Vector(100, -20, 10,  5,  5, 10, -20, 100),
    Vector(-20, -50, -2, -2, -2, -2, -50, -20),
    Vector(10,   -2, -1, -1, -1, -1,  -2,  10),
    Vector(5,    -2, -1, -1, -1, -1,  -2,   5),

    Vector(5,    -2, -1, -1, -1, -1,  -2,   5),
    Vector(10,   -2, -1, -1, -1, -1,  -2,  10),
    Vector(-20, -50, -2, -2, -2, -2, -50, -20),
    Vector(100, -20, 10,  5,  5, 10, -20, 100)
  )

  def evaluate(board: Board): Int = {
    val bP = if (p.value == 1) new Player(2) else new Player(1)
    if (board.count._1 + board.count._2 <= 32) board.count(bP.value) - board.count(p.value)
    else if (board.gameOver) board.count(p.value) - board.count(bP.value)
    else (for {
      col <- weightedBoard.indices
      row <- weightedBoard.indices
      if board.setBy(p.value, col, row)
    } yield weightedBoard(row)(col)).sum
  }

  def max(x: Move, y: Move): Move = if (x._1 >= y._1) x else y

  def min(x: Move, y: Move): Move = if (x._1 <= y._1) x else (y._1, x._2)
}


trait MinMax
case object Min extends MinMax
case object Max extends MinMax

object MoveSelector {
  def main(args: Array[String]): Unit = {
    val controller = new Controller
    val tui = new Tui(controller)
    controller.setupPlayers("0")
    controller.newGame()
  }
}
