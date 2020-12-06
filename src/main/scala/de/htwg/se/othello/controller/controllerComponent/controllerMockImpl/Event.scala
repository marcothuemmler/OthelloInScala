package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus.{GameStatus, IDLE}
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.swing.event.Event

class BoardChanged extends Event

class ModeChanged(playerCount: String) extends Event {
  def mode: JsObject = Json.obj("mode" -> playerCount)
}

class CurrentPlayerChanged(val player: JsValue) extends Event

class DifficultyChanged(diff: String) extends Event {
  def difficulty: JsObject = Json.obj("difficulty" -> diff)
}

class GameStatusChanged(old: GameStatus, newStatus: GameStatus) extends Event {
  def this(newStatus: GameStatus) = {
    this(IDLE, newStatus)
  }
  def status = Json.obj("old_status" -> old, "new_status" -> newStatus)
}
