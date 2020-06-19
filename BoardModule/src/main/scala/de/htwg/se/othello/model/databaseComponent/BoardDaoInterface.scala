package de.htwg.se.othello.model.databaseComponent

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait BoardDaoInterface {

  def save(board:BoardInterface): Unit
  def load(): BoardInterface

}
