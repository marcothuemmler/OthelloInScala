package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val game = Logic(new Board)
    val p1 = Player("Player1", 1)
    val p2 = Player("Player2", 2)
    game.flip(2, 3, p1)
    game.board.flip(3,3, 1)
    println(game)
  }
}
