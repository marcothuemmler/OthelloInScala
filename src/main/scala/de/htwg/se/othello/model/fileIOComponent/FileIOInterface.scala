package de.htwg.se.othello.model.fileIOComponent

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface

trait FileIOInterface {

  def load: (BoardInterface, Player, Int)
  def save(board: BoardInterface, player: Player, difficulty: Int): Unit

}
