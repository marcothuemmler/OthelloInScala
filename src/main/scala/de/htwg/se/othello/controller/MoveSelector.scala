package de.htwg.se.othello.controller
import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.model.{Board, Player}

final class MoveSelector(p: Player) {

  private trait MinMax
  private object Min extends MinMax
  private object Max extends MinMax

  private type Move = (Int, Option[(Int, Int)])
  private val betaP: Player = if (p.value == 1) new Player(2) else new Player(1)

  def search(board: Board, player: Player, depth: Int = 4): (Int, Int) = {
    val res = alphaBeta(depth, board, None, player, -100000, 100000, Max)
    println(player + "  " + res)
    res._2.getOrElse(board.moves(player.value).values.flatten.head)
  }

  private def alphaBeta(d: Int, n: Board, choice: Option[(Int, Int)], pl: Player, alpha: Int, beta: Int, m: MinMax): Move = {
    println(pl + "   " + "   " + alpha + "   " + "   " + beta + "   " + choice+ "   depht:   " + d)
    if (d == 0 || n.gameOver) {
      val result = (evaluate(pl, n), choice)
      println("reached depth:   " + result)
      result
    }
    else if (m == Max) {
      n.moves(pl.value).values.flatten.toSet.takeWhile(_ => beta > alpha).foldLeft(alpha, choice) {
        case ((a, select), move) =>
          val board = simulate(n, pl, move)
          println("maxboard:  \n" + board)
          max((a, select), alphaBeta(d - 1, board, Option(move), betaP, a, beta, Min))
      }
    } else {
      n.moves(pl.value).values.flatten.toSet.takeWhile(_ => beta > alpha).foldLeft((beta, choice)) {
        case ((b, select), move) =>
          val board = simulate(n, pl, move)

          println("minboard:  \n" + board)
          min((b, select), alphaBeta(d - 1, board, Option(move), p, alpha, b, Max))
      }
    }
  }

  private def simulate(node: Board, player: Player, move: (Int, Int)): Board = {
    val legal = node.moves(player.value).filter(o => o._2.contains(move))
    var board = node
    for { fromSquare <- legal.keys }
      board = node.flipLine(fromSquare, move, player.value)
    board
  }

  private def evaluate(player: Player,board: Board): Int = board.count(player.value)

  private def max(x: Move, y: Move): Move = if (x._1 >= y._1) x else y

  private def min(x: Move, y: Move): Move = if (x._1 <= y._1) x else (y._1, x._2)
}

object MoveSelector {
  def main(args: Array[String]): Unit = {
    val controller = new Controller
    controller.createBoard(4)
    val tui = new Tui(controller)
    controller.setupPlayers("0")
    controller.newGame()
  }
}