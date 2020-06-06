package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.model.boardComponent.BoardInterface
import play.api.libs.json.{JsObject, JsValue}

trait BoardControllerInterface {
  var board: BoardInterface
  def size: Int
  def resizeBoard: String =>  Unit
  def createBoard(size: Int): Unit
  def changeHighlight(implicit value: Int): Unit
  def moves(implicit value: Int): Map[(Int, Int), Seq[(Int, Int)]]
  def gameOver: Boolean
  def valueOf(col: Int, row: Int): Int
  def count(value: Int): Int
  def boardToString: String
  def boardToHtml: String
  def toJson: JsObject
  def movesToJson(implicit value: Int): JsValue

}
