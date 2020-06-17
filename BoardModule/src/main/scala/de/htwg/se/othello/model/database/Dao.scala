package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait Dao {

  def save(board:BoardInterface)
  def load(): BoardInterface

}
