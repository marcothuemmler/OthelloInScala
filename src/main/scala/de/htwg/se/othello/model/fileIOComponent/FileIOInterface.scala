package de.htwg.se.othello.model.fileIOComponent

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface

import scala.util.Try

trait FileIOInterface {

  def load(dir: String): Try[(BoardInterface, Player, String)]
  def save(dir: String)(implicit board: BoardInterface, player: Player, difficulty: String): Unit

}
