package de.htwg.se.othello

object Othello {

  def main(args: Array[String]): Unit = {

    val game = new Game
    val p1 = Player("Player1", 1)
    val p2 = Player("Player2", 2)
    game.update()
    game.flip(2,3,p1)
    game.flip(2,2,p2)
    game.flip(5,4,p1)
    game.flip(3,5,p2)
  }
}
