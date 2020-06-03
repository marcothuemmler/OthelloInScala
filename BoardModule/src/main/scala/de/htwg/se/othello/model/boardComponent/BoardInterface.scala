package de.htwg.se.othello.model.boardComponent

import play.api.libs.json.JsObject

trait BoardInterface {

  val gameOver: Boolean
  val size: Int
  def moves(implicit value: Int): Map[(Int, Int), Seq[(Int, Int)]]
  def changeHighlight(implicit value: Int): BoardInterface
  def deHighlight: BoardInterface
  def toString: String
  def valueOf(col: Int, row: Int): Int
  def count(value: Int): Int
  def flipLine(fromSquare: (Int, Int), toSquare: (Int, Int), value: Int): BoardInterface
  def toHtml: String
  def toJson: JsObject

}

trait BoardFactory {
  def create(size: Int): BoardInterface
}
