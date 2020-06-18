package de.htwg.se.othello.model.databaseComponent

import de.htwg.se.othello.model.Player

trait PlayerDaoInterface {

  def save(currentPlayer: Player, otherPlayer: Player): Unit
  def load(): Vector[Player]

}
