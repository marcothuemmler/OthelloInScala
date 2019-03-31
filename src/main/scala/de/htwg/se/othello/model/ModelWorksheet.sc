import de.htwg.se.othello.{Board, Logic, Player}

val board = new Board
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
board.flip(5, 3, player1.value)
board.flip(3, 3, player1.value)

val game = Logic(board)
println("game.checkTile(3,4, player1): " + game.checkMoves(3,4, player1))
println("game.checkTile(4,4, player2): " + game.checkMoves(4,4, player2))
println("game.checkTile(3,3, player2): " + game.checkMoves(3,3, player2))
println(board)
