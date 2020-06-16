package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.boardComponent.BoardInterface

trait Dao {

  def create(board:BoardInterface)
  def read(board:BoardInterface)
  def update(board:BoardInterface)
  def delete(board:BoardInterface)

}
