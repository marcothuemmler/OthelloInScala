package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait CreateBoardTemplate {

  def createNewBoard(size: Int): BoardInterface = {
    var board: BoardInterface = new Board(size)
    board = prepare(board)
    board = fill(board)
    board = postProcess(board)
    board
  }

  def prepare(board: BoardInterface): BoardInterface = board

  def fill(board: BoardInterface): BoardInterface

  def postProcess(board: BoardInterface): BoardInterface = board
}
