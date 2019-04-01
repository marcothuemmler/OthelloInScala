import de.htwg.se.othello.{Game, Player}

val game = new Game
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
game.flip(5, 4, player1)
game.moves(player1)
println(game)
println(game.moves(player1).filter(entry => entry._2.contains((5,4))).keys)
