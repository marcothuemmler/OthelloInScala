import de.htwg.se.othello.{Game, Player}

val game = new Game
val p1 = Player("Player1", 1)
val p2 = Player("Player2", 2)
game.update()
game.flip(2,3,p1)
game.flip(2,2,p2)
game.flip(3,2,p1)
game.flip(0,0,p1)
