package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus.{GameStatus, ILLEGAL}

import scala.swing.event.Event

class BoardChanged extends Event
class ModeChanged(playerCount: String) extends Event
class CurrentPlayerChanged(to: Int) extends Event
class DifficultyChanged(difficulty: String) extends Event
class GameStatusChanged(from: GameStatus, to: GameStatus) extends Event {
  def this(to: GameStatus) {
    this(ILLEGAL, to)
  }
}
