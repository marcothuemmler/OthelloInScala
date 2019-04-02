package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val game = new Game
    val p1 = Player("Player1", 1, game)
    val p2 = Player("Player2", 2, game)
    p1.set(2, 3)
    p2.set(2, 2)
    p1.set(3, 2)
    p2.set(2, 4)
  }
}
