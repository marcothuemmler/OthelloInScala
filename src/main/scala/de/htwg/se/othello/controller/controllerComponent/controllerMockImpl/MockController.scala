package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.othello.model.Player

import scala.concurrent.{ExecutionContext, Future}

class MockController extends ControllerInterface {

  override var gameStatus: GameStatus = _
  override var difficulty: String = _

  override def currentPlayer: Player = Player("John" ,1)

  override def nextPlayer: Player = Player("Bob", 2)

  override def size: Int = 8

  override def suggestions: String = "suggestion"

  override def score: String = "Bob wins"

  override def boardToString: String = "boardString"

  override def boardToHtml: String = "boardHtml"

  override def moves: Map[(Int, Int), Seq[(Int, Int)]] = Map((1, 2) -> Seq((3, 4)))

  override def gameOver: Boolean = false

  override def canUndo: Boolean = false

  override def canRedo: Boolean = false

  override def playerCount: Int = 2

  override def count(value: Int): Int = 2

  override def valueOf(col: Int, row: Int): Int = 42

  override def newGame: Future[Unit] = Future(())(ExecutionContext.global)

  override def illegalAction(): Unit = ()

  override def resizeBoard(op: String): Unit = ()

  override def setupPlayers: String => Unit = _ => ()

  override def setDifficulty(value: String): Unit = ()

  override def set(square: (Int, Int)): Unit = ()

  override def undo(): Unit = ()

  override def redo(): Unit = ()

  override def highlight(): Unit = ()

  override def save(): Unit = ()

  override def load(): Unit = ()
}
