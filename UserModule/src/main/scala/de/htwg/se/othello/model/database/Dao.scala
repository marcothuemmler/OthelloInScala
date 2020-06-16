package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.Player

trait Dao {

  def save(players: Vector[Player]): Unit
  def load(): Vector[Player]

}
