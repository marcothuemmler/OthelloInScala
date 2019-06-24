package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.othello.model.Player

import scala.concurrent.Future
import scala.swing.Publisher
import scala.swing.event.Event

trait ControllerInterface extends Publisher {

  var gameStatus: GameStatus
  def player: Player
  def difficulty: Int
  def isReady: Boolean
  def size: Int
  def resizeBoard(op: String): Unit
  def setupPlayers: String => Unit
  def setDifficulty(value: String): Unit
  def newGame: Future[Unit]
  def set(square: (Int, Int))
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

}

class BoardChanged extends Event
class PlayerOmitted(player: Player) extends Event
