package de.htwg.se.othello.model

class CreateBoardStrategy extends CreateBoardTemplate {
  override def fill(board: Board): Board = Board(Vector.tabulate(board.size, board.size)((i, j) => {
    if ((i == board.size / 2 || j == board.size /2 - 1) && i == j) Square(2)
    else if (i == board.size /2 && j == (board.size/2)-1 || i == (board.size/2)-1 && j == board.size/2) Square(1)
    else Square(0)
  }))
}
