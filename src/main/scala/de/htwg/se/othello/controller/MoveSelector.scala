package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}

import scala.util.Try

class MoveSelector(controller: Controller) {
  val alphaPlayer: Player = controller.player
  val betaPlayer: Player = {
    if (alphaPlayer == controller.players(0)) controller.players(1)
    else controller.players(0)
  }

  def options: List[(Int, Int)] = controller.options

  def select = Try(options(scala.util.Random.nextInt(options.size)))

  def evaluate(board: Board): Int = {
    (for {
      col <- weightedBoard.indices
      row <- weightedBoard.indices
      if board.setBy(alphaPlayer.value, col, row)
    } yield weightedBoard(row)(col)).sum
  }

  def max(x: (Int, (Int, Int)), y: (Int, (Int, Int))): (Int, (Int, Int)) = if (x._1 >= y._1) x else y
  def min(x: (Int, (Int, Int)), y: (Int, (Int, Int))): (Int, (Int, Int)) = if (x._1 <= y._1) x else (y._1, x._2)

  def alphabeta(depth: Int, node: Board, moveChoice: (Int, Int), player: Player, alpha: Int, beta: Int): (Int, (Int, Int)) = {
    if (depth == 0 || node.moves(player.value).isEmpty) {
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
              max((alpha, moveChoice), alphabeta(depth - 1, test, move, betaPlayer, alpha, beta))
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
              } test = node.flipLine(fromSquare, moveChoice, player.value)
              min((beta, moveChoice), alphabeta(depth - 1, test, move, alphaPlayer, alpha, beta))
          }
    }
  }

  def search(controller: Controller, player: Player, MAX_DEPTH: Int): (Int, Int) = {
    val v = alphabeta(MAX_DEPTH, controller.board, (0, 0), alphaPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE)
    v._2
  }

  val weightedBoard: Vector[Vector[Int]] = Vector(
    Vector(99, -8, 8, 6, 6, 8, -8, 99),
    Vector(-8, -24, -4, -3, - 3, -4, -24, -8),
    Vector(8, -4, 7, 4, 4, 7, -4, 8),
    Vector(6, -3, 4, 0, 0, 4, -3, 6),

    Vector(6, -3, 4, 0, 0, 4, -3, 6),
    Vector(8, -4, 7, 4, 4, 7, -4, 8),
    Vector(-8, -24, -4, -3, -3, -4, -24, -8),
    Vector(99, -8, 8, 6, 6, 8, -8, 99)
  )
}

object MoveSelector {
  def main(args: Array[String]): Unit = {
    val controller = new Controller
    val moveSelector = new MoveSelector(controller)
    /*controller.set(2,3)
    controller.set(2,4)
    controller.set(2,5)
    controller.set(1,4)
    controller.set(3,5)
    controller.set(2,6)
    controller.set(1,6)
    controller.set(0,6)
    controller.set(0,7)*/
    controller.setupPlayers("0")
    controller.newGame()
  }
}
