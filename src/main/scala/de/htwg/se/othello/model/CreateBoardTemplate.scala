package de.htwg.se.othello.model

trait CreateBoardTemplate {

  def createNewBoard(size: Int): Board = {
    var grid = new Board(size)
    grid = prepare(grid)
    grid = fill(grid)
    grid = postProcess(grid)
    grid
  }

  def prepare(board: Board): Board = board

  def fill(board: Board): Board // abstract

  def postProcess(board: Board): Board = board
}
