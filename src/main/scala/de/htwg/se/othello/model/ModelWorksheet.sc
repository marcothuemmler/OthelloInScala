import de.htwg.se.othello.{Board, Player}

val game = new Board
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
game.flip(5, 3, player1)
println(game)
