package de.htwg.se.othello.model.fileIOComponent

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface

import scala.util.Try

trait FileIOInterface {

  def load: Try[(BoardInterface, Player, String)]
  def save(board: BoardInterface, player: Player, difficulty: String): Unit

}
