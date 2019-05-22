package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}

class MoveSelector(controller: Controller) {
  type evalMove = (Int, Option[(Int, Int)])
  val alphaPlayer: Player = controller.player
  val betaPlayer: Player = {
    if (alphaPlayer == controller.players(0)) controller.players(1)
    else controller.players(0)
  }
  val weightedBoard: Vector[Vector[Int]] = Vector(
    Vector(99, -8, 8, 6, 6, 8, -8, 99),
    Vector(-8, -24, -4, -3, -3, -4, -24, -8),
    Vector(8, -4, 7, 4, 4, 7, -4, 8),
    Vector(6, -3, 4, 0, 0, 4, -3, 6),

    Vector(6, -3, 4, 0, 0, 4, -3, 6),
    Vector(8, -4, 7, 4, 4, 7, -4, 8),
    Vector(-8, -24, -4, -3, -3, -4, -24, -8),
    Vector(99, -8, 8, 6, 6, 8, -8, 99)
  )

  def evaluate(board: Board): Int = {
    (for {
      col <- weightedBoard.indices
      row <- weightedBoard.indices
      if board.setBy(alphaPlayer.value, col, row)
    } yield weightedBoard(row)(col)).sum
  }

  def max(x: evalMove, y: evalMove): evalMove = if (x._1 >= y._1) x else y

  def min(x: evalMove, y: evalMove): evalMove = {
    if (x._1 <= y._1) x else (y._1, x._2)
  }

  def alphaBeta(depth: Int, node: Board, moveChoice: Option[(Int, Int)], player: Player, alpha: Int, beta: Int): evalMove = {
    if (depth == 0 || node.gameOver || node.moves(player.value).isEmpty) {
      (evaluate(node), moveChoice)
    } else player.value match {
      case alphaPlayer.value =>
        node.moves(player.value).values.flatten.toSet.
          takeWhile(_ => beta > alpha).
          foldLeft((alpha, moveChoice)) {
            case ((alpha, moveChoice), move) =>
              val legal = node.moves(player.value).filter(o => o._2.contains(move))
              var test = node
              for {
                fromSquare <- legal.keys
              } test = node.flipLine(fromSquare, move, player.value)
              max((alpha, moveChoice), alphaBeta(depth - 1, test, Option(move), betaPlayer, alpha, beta))
          }
      case betaPlayer.value =>
        node.moves(player.value).values.flatten.toSet.
          takeWhile(_ => beta > alpha).
          foldLeft((beta, moveChoice)) {
            case ((beta, _), move) =>
              val legal = node.moves(player.value).filter(o => o._2.contains(move))
              var test = node
              for {
                fromSquare <- legal.keys
              } test = node.flipLine(fromSquare, moveChoice.get, player.value)
              min((beta, moveChoice), alphaBeta(depth - 1, test, Option(move), alphaPlayer, alpha, beta))
          }
    }
  }

  def search(board: Board, player: Player, MAX_DEPTH: Int): Option[(Int, Int)] = {
    val v = alphaBeta(MAX_DEPTH, controller.board, None, alphaPlayer, -1000, 1000)
    println(v)
    v._2
  }
}

object MoveSelector {
  def main(args: Array[String]): Unit = {
    val controller = new Controller
    controller.setupPlayers("0")
    controller.newGame()
  }
}
