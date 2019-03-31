import de.htwg.se.othello.{Board, Logic, Player}

val board = new Board
val player1 = Player("Player1", 1)
val player2 = Player("Player2", 2)
println(f"$player1%s \n$player2%s")
board.flip(5, 3, player1.value)
board.flip(3, 3, player1.value)

val game = Logic(board)
println("game.checkNorth(3,4, player1): " + game.checkNorth(3,4, player1))
println("game.checkRight(3,4, player1): " + game.checkRight(3,4, player1))
println("game.checkRight(4,4, player2): " + game.checkRight(4,4, player2))
println("game.checkNorth(4,4, player2): " + game.checkNorth(4,4, player2))
println("game.checkNorth(3,3, player2): " + game.checkNorth(3,3, player2))
println(board)
