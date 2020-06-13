package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.Player

trait Dao {

  def create(player:Player)
  def read()
  def update(player:Player)
  def delete(player:Player)

}
