package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus.{GameStatus, ILLEGAL}
import play.api.libs.json.JsValue

import scala.swing.event.Event

class BoardChanged extends Event
class BoardSizeChanged(op: String) extends Event
class BoardHighlightChanged(currentPlayerValue: Int, moves: JsValue) extends Event
class CellsChanged(cells: JsValue) extends Event
class ModeChanged(playerCount: String) extends Event
class CurrentPlayerChanged(to: Int) extends Event
class DifficultyChanged(difficulty: String) extends Event
class GameStatusChanged(from: GameStatus, to: GameStatus) extends Event {
  def this(to: GameStatus) {
    this(ILLEGAL, to)
  }
}
