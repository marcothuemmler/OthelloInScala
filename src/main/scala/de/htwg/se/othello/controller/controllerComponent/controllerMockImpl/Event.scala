package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus.{GameStatus, IDLE}
import play.api.libs.json.{JsValue, Json}

import scala.swing.event.Event

class NewGameCreated extends Event

class BoardChanged extends Event

class BoardSizeChanged(op: String) extends Event {
  def operation = Json.obj("operation" -> op)
}

class BoardHighlightChanged(currentPlayerValue: Int, possibleMoves: Seq[(Int, Int)]) extends Event {
  def moves = Json.obj(
    "currentPlayer" -> currentPlayerValue,
    "squares" -> Json.toJson(for {
      (row, col) <- possibleMoves
    } yield Json.obj(
        "value" -> -1,
        "row" -> row,
        "col" -> col
      )
    )
  ).toString
}

class CellsChanged(val cells: JsValue) extends Event

class ModeChanged(playerCount: String) extends Event {
  def mode = Json.obj("mode" -> playerCount).toString
}

class CurrentPlayerChanged(to: Int) extends Event {
  def newPlayer = Json.obj("player" -> to).toString
}

class DifficultyChanged(diff: String) extends Event{
  def difficulty = Json.obj("difficulty" -> diff).toString
}

class GameStatusChanged(old: GameStatus, newStatus: GameStatus) extends Event {
  def this(newStatus: GameStatus) {
    this(IDLE, newStatus)
  }
  def status = Json.obj("old" -> old, "new" -> newStatus).toString
}
