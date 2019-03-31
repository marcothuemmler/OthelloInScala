import de.htwg.se.othello.{Board, Logic, Player}

val board = new Board
val game = Logic(board)
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
board.flip(5, 3, player1.value)
board.flip(3, 3, player1.value)
game.validMoves(player1)
println(board)
println(game.validMoves(player1).filter(entry => entry._2.contains((5,4))).keys)