package de.htwg.se.othello.model.boardComponent

trait BoardInterface {

  def moves(value: Int): Map[(Int, Int), Seq[(Int, Int)]]
  def changeHighlight(value: Int): BoardInterface
  def deHighlight: BoardInterface
  def gameOver: Boolean
  def toString: String
  def valueOf(col: Int, row: Int): Int
  def size: Int
  def count(value: Int): Int
  def indices: Range
  def setBy(value: Int, col: Int, row: Int): Boolean
  def flipLine(fromSquare: (Int, Int), toSquare: (Int, Int), value: Int): BoardInterface

}
