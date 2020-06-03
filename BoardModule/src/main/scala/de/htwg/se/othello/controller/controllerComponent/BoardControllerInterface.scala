package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.model.boardComponent.BoardInterface
import play.api.libs.json.JsObject

trait BoardControllerInterface {
  var board: BoardInterface
  def size: Int
  def resizeBoard(op: String): Unit
  def createBoard(size: Int): Unit
  def boardToString: String
  def boardToHtml: String
  def toJson: JsObject

}
