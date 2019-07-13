package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.othello.model.Player
import de.htwg.se.othello.util.Observable

import scala.concurrent.Future

trait ControllerInterface extends Observable {

  var gameStatus: GameStatus
  var difficulty: Int
  var player: Player
  def size: Int
  def resizeBoard(op: String): Unit
  def setupPlayers: String => Unit
  def setDifficulty(value: String): Unit
  def newGame: Future[Unit]
  def set(square: (Int, Int)): Unit
  def undo(): Unit
  def redo(): Unit
  def highlight(): Unit
  def suggestions: String
  def options: Seq[(Int, Int)]
  def moves: Map[(Int, Int), Seq[(Int, Int)]]
  def gameOver: Boolean
  def playerCount: Int
  def boardToString: String
  def valueOf(col: Int, row: Int): Int
  def count(value: Int): Int
  def score: String
  def save(): Unit
  def load(): Unit

}
