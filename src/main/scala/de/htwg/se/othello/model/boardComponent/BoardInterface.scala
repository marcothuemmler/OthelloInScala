package de.htwg.se.othello.model.boardComponent

trait BoardInterface {

  def moves(value: Int): Map[(Int, Int), Seq[(Int, Int)]]
  def isHighlighted: Boolean
  def highlight(value: Int): BoardInterface
  def deHighlight: BoardInterface
  def gameOver: Boolean
  def toString: String
  def valueOf(col: Int, row: Int): Int
  def score: String
  def size: Int
  def count(value: Int): Int
  def indices: Range
  def setBy(value: Int, col: Int, row: Int): Boolean



  // Only used for testing purposes...
  def flip(col: Int, row: Int, value: Int): BoardInterface
  def flipLine(fromSquare: (Int, Int), toSquare: (Int, Int), value: Int): BoardInterface

}
