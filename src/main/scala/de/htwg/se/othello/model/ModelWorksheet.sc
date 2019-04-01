import de.htwg.se.othello.{Board, Logic, Player}

val game = Logic(new Board)
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
game.flip(5, 3, player1)
game.flip(3, 3, player1)
game.validMoves(player1)
println(game)
println(game.validMoves(player1).filter(entry => entry._2.contains((5,4))).keys)
