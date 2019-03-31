package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val board = new Board
    val player1 = Player("Player1", 1)
    val player2 = Player("Player2", 2)
    println(board)
    println(f"$player1%s \n$player2%s")
  }
}
