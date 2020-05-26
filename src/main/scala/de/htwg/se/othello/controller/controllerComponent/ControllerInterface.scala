package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.othello.model.Player
import de.htwg.se.othello.util.Observable

import scala.concurrent.Future

trait ControllerInterface extends Observable {

  var gameStatus: GameStatus
  var difficulty: String
  def getCurrentPlayer: Player
  def nextPlayer: Player
  def size: Int
  def suggestions: String
  def score: String
  def boardToString: String
  def boardToHtml: String
  def moves: Map[(Int, Int), Seq[(Int, Int)]]
  def gameOver: Boolean
  def canUndo: Boolean
  def canRedo: Boolean
  def playerCount: Int
  def count(value: Int): Int
  def valueOf(col: Int, row: Int): Int
  def newGame: Future[Unit]
  def illegalAction(): Unit
  def resizeBoard(op: String): Unit
  def setupPlayers: String => Unit
  def setDifficulty(value: String): Unit
  def set(square: (Int, Int)): Unit
  def undo(): Unit
  def redo(): Unit
  def highlight(): Unit
  def save(): Unit
  def load(): Unit

}
