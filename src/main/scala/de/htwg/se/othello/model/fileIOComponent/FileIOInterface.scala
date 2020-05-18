package de.htwg.se.othello.model.fileIOComponent

import boardComponent.{BoardInterface, Player}

import scala.util.Try

trait FileIOInterface {

  def load: Try[(BoardInterface, Player, String)]
  def save(board: BoardInterface, player: Player, difficulty: String): Unit

}
