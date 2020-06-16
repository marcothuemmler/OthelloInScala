package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import play.api.libs.json.JsValue

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

  override def fill(boardJson: JsValue): BoardInterface = {
    val size = (boardJson \ "size").as[Int]
    var board = createNewBoard(size)
    for {
      index <- 0 until size * size
      row = (boardJson \\ "row") (index).as[Int]
      col = (boardJson \\ "col") (index).as[Int]
      value = (boardJson \\ "value") (index).as[Int]
    } board = board.flipLine((row, col), (row, col))(value)
    board
  }
}
