package de.htwg.se.othello.model.boardComponent.controller.controllerBaseImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.controller.BoardControllerInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy

class BoardController extends BoardControllerInterface {

  var board: BoardInterface = (new CreateBoardStrategy).createNewBoard(8)

  def size: Int = board.size

  def resizeBoard(op: String): Unit = {
    op match {
      case "+" => createBoard(size + 2)
      case "-" => if (size > 4) createBoard(size - 2)
      case "." => if (size != 8) createBoard(8)
    }
  }

  def createBoard(size: Int): Unit = {
    board = (new CreateBoardStrategy).createNewBoard(size)
  }

  def boardToString: String = board.toString

  def boardToHtml: String = board.toHtml
}
