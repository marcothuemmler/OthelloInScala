package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface

class CreateBoardStrategy extends CreateBoardTemplate {
  override def fill(board: BoardInterface): BoardInterface = {
    val size = board.size
    val half = size / 2
    Board(Vector.tabulate(size, size)((i, j) =>
      if ((i == half || j == half - 1) && i == j) Square(2)
      else if (i == half && j == half - 1 || i == half - 1 && j == half) Square(1)
      else Square(0)
    ))
  }
}
