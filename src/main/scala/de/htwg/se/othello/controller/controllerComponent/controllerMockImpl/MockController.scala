package de.htwg.se.othello.controller.controllerComponent.controllerMockImpl

import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy

import scala.concurrent.{ExecutionContext, Future}

class MockController extends Controller {

  var board: BoardInterface = new CreateBoardStrategy().createNewBoard(8)

  var players = Seq(Player("John", 1), Player("Bob", 2))

  var player: Player = players.head

  override def currentPlayer: Player = player

  override def nextPlayer: Player = if (currentPlayer == players.head) players(1) else players.head

  override def size: Int = board.size

  override def boardToString: String = board.toString

  override def boardToHtml: String = board.toHtml

  override def moves: Map[(Int, Int), Seq[(Int, Int)]] = board.moves(currentPlayer.value)

  override def gameOver: Boolean = board.gameOver

  override def playerCount: Int = 2

  override def count(value: Int): Int = board.count(value)

  override def valueOf(col: Int, row: Int): Int = board.valueOf(col, row)

  override def newGame: Future[Unit] = Future(())(ExecutionContext.global)

  override def resizeBoard(op: String): Unit = ()

  override def setupPlayers: String => Unit = _ => ()

  override def highlight(): Unit = board = board.changeHighlight(currentPlayer.value)

  override def save(dirOption: Option[String]): Unit = super.save(Some("savegame.xml"))

  override def load(dirOption: Option[String]): Unit = super.load(Some("savegame.xml"))

  override def setCurrentPlayer(player: Player): Unit = this.player = player

  override def getPlayer(isFirstPlayer: Boolean): Player = if (isFirstPlayer) players.head else players(1)

  def createBoard(size: Int): Unit = board = new CreateBoardStrategy().createNewBoard(size)

  override def getBoard: BoardInterface = board

  override def setBoard(board: BoardInterface): Unit = this.board = board
}
