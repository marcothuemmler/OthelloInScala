package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val board = new Board
    val p1 = Player("Player1", 1)
    val p2 = Player("Player2", 2)
    val game = Logic(board)
    println("Player1 moves: " + game.validMoves(p1))
    println("Player2 moves: " + game.validMoves(p2))
    println(game.validMoves(p1).filter(entry => entry._2.contains((2,3))).keys)
    board.flip(2, 3, 1)
    board.flip(3, 3, 1)
    println(board)
  }
}
