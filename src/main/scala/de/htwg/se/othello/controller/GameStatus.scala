package de.htwg.se.othello.controller

object GameStatus extends Enumeration {
  type GameStatus = Value
  val IDLE, ILLEGAL, OMITTED, GAME_OVER, DIFFICULTY_CHANGED = Value

  val map: Map[GameStatus, String] = Map[GameStatus, String](
    IDLE -> "",
    ILLEGAL -> "Please try again",
    OMITTED -> "No legal moves",
    GAME_OVER -> "\nPress \"n\" for new game",
    DIFFICULTY_CHANGED -> "Difficulty changed"
  )

  def message(gameStatus: GameStatus): String = map(gameStatus)
}
