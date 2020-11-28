package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus.{GameStatus, ILLEGAL}
import play.api.libs.json.JsValue

import scala.swing.event.Event

class NewGameCreated extends Event
class BoardChanged extends Event
class BoardSizeChanged(val op: String) extends Event
class BoardHighlightChanged(val currentPlayerValue: Int, val moves: JsValue) extends Event
class CellsChanged(val cells: JsValue) extends Event
class ModeChanged(val playerCount: String) extends Event
class CurrentPlayerChanged(val to: Int) extends Event
class DifficultyChanged(val difficulty: String) extends Event
class GameStatusChanged(val from: GameStatus, val to: GameStatus) extends Event {
  def this(to: GameStatus) {
    this(ILLEGAL, to)
  }
}
