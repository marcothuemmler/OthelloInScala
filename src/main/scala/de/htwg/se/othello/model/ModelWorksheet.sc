import de.htwg.se.othello.{Board, Player}

val board = new Board
val player1 = Player("player1", 1)

board.flip(5, 2, player1)
