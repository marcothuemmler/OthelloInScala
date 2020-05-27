package de.htwg.se.othello.model.boardComponent.controller

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait BoardControllerInterface {
  var board: BoardInterface
  def size: Int
  def resizeBoard(op: String): Unit
  def createBoard(size: Int): Unit
  def boardToString: String
  def boardToHtml: String

}
