package de.htwg.se.othello.model

trait CreateBoardTemplate {

  def createNewBoard(size:Int): Board = {
    var board = new Board
    board = prepare(board)
    board = fill(board)
    board = postProcess(board)
    board
  }

  def prepare(grid: Board):Board = {
    // by default do nothing
    grid
  }

  def fill(board: Board) : Board // abstract

  def postProcess(board: Board):Board = { // default implementation
    board
  }
}
