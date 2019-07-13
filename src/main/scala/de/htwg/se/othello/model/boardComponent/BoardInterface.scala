package de.htwg.se.othello.model.boardComponent

trait BoardInterface {

  val gameOver: Boolean
  val size: Int
  def moves(value: Int): Map[(Int, Int), Seq[(Int, Int)]]
  def changeHighlight(value: Int): BoardInterface
  def deHighlight: BoardInterface
  def toString: String
  def valueOf(col: Int, row: Int): Int
  def count(value: Int): Int
  def flipLine(fromSquare: (Int, Int), toSquare: (Int, Int), value: Int): BoardInterface

}

trait BoardFactory {
  def create(size: Int): BoardInterface
}
