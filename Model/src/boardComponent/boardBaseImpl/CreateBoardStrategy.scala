package boardComponent.boardBaseImpl

import boardComponent.BoardInterface

class CreateBoardStrategy extends CreateBoardTemplate {
  override def fill(size: Int): BoardInterface = {
    //val size = size
    val half = size / 2
    Board(Vector.tabulate(size, size)((i, j) =>
      if ((i == half || j == half - 1) && i == j) Square(2)
      else if (i == half && j == half - 1 || i == half - 1 && j == half) Square(1)
      else Square(0)
    ))
  }
}
