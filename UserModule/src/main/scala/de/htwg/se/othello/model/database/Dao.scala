package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.Player

trait Dao {

  def create(player:Player): Unit
  def read(): Player
  def update(player:Player): Unit
  def delete(player:Player): Unit

}
