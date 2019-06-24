package de.htwg.se.othello.model.fileIOComponent

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait FileIOInterface {

  def load: BoardInterface
  def save(board: BoardInterface): Unit

}
