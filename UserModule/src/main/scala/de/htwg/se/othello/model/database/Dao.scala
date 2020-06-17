package de.htwg.se.othello.model.database

import de.htwg.se.othello.model.Player

trait Dao {

  def save(currentPlayer: Player, otherPlayer: Player): Unit
  def load(): Vector[Player]

}
