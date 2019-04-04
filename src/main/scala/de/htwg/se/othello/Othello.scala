package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val game = Game(new Board)
    val player1 = new Player("Player1", 1, game)
    val player2 = new Player("Player2", 2, game)
    player1.set(2, 3)
    player2.set(2, 2)
    player1.set(3, 2)
    player2.set(2, 4)
    player1.set(5, 5)
    player2.set(5, 3)
    player1.set(1, 4)
    player2.set(2, 5)
    player1.set(1, 2)
    player2.set(0, 4)
    player1.set(0, 5)
    player2.set(0, 3)
    player1.set(0, 2)
  }
}
