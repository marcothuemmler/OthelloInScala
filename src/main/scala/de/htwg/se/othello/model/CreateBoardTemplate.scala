package de.htwg.se.othello.model

trait CreateBoardTemplate {

  def createNewBoard(size: Int): Board = {
    var board = new Board(size)
    board = prepare(board)
    board = fill(board)
    board = postProcess(board)
    board
  }

  def prepare(board: Board): Board = board

  def fill(board: Board): Board

  def postProcess(board: Board): Board = board
}
