package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Bot, CreateBoardStrategy, Player, Square}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  var c = new Controller(players)
  val strategy = new CreateBoardStrategy
  val b: Board = strategy.createNewBoard(8)

  "A controller created without board parameter" should {
    "have an empty board of size 8x8" in {
      val ctrl = new Controller(players)
      ctrl.board should equal(new Board)
    }
  }
  "newGame" should {
    "reset the board" in {
      c.set(2, 3)
      c.board should not equal b
      c.newGame()
      c.board should equal(b)
      c.player should be(c.players(0))
    }
    "reset the board and make the first move if the first player ist a Bot" in {
      val ctrl = new Controller(Vector(new Bot(1), new Player(2)))
      val newGame = Future(ctrl.newGame())(ExecutionContext.global)
      Await.ready(newGame, Duration.Inf)
      if (ctrl.isReady) ctrl.player should not be ctrl.players(0)
    }
  }
  "set" should {
    "set one disk and flip at least one of the opponents disks" in {
      c.newGame()
      c.set(2, 3)
      c.board.count should be(4, 1)
    }
    "not change any square on the board if the input is incorrect" in {
      c.newGame()
      c.set(0, 0)
      c.board should equal(b)
    }
    "skip the player if there are no valid moves to be made" in {
      c.board = new Board
      val emptyBoard = c.board
      c.set(0, 0)
      c.board should equal(emptyBoard)
      c.newGame()
    }
    "omit a player who doesn't have valid moves" in {
      val controller = new Controller(Vector(new Player(1), new Player(2)))
      controller.board = Board(Vector.fill(8, 8)(Square(1)))
      controller.board = controller.board.flipLine((0, 3), (0, 6), 2)
      controller.board = controller.board.flip(0, 7, 0)
      controller.board = controller.board.flip(7, 0, 0)
      controller.board = controller.board.flipLine((6, 0), (6, 1), 2)
      controller.board = controller.board.flip(7, 1, 2)
      val currentPlayer = controller.player
      controller.set(7, 0)
      controller.player should equal(currentPlayer)
    }
  }
  "selectAndSet" should {
    "not change any square on the board if the player has no valid move" in {
      c.setupPlayers("1")
      c.board = new Board
      c.board = c.board.flipLine((0, 7), (5, 7), 1)
      c.board = c.board.flipLine((0, 6), (0, 5), 1)
      c.board = c.board.flipLine((1, 6), (3, 6), 2)
      val cBoard = c.board
      c.player = c.players(1)
      c.selectAndSet()
      c.board should equal(cBoard)
      c.newGame()
    }
  }
  "omit" should {
    "change the current player and set the gameStatus to omitted" in {
      val ctrl = new Controller
      val currentPlayer = ctrl.player
      ctrl.omitPlayer()
      ctrl.player should not equal currentPlayer
      ctrl.gameStatus should equal (GameStatus.OMITTED)
    }
  }
  "undo" should {
    "revert the board to a previous state" in {
      val ctrl = new Controller
      ctrl.createBoard(8)
      ctrl.set(3, 2)
      ctrl.undo()
      ctrl.board should equal(b)
    }
  }
  "redo" should {
    "redo undone changes" in {
      val ctrl = new Controller
      ctrl.createBoard(8)
      ctrl.set(3, 2)
      val changedBoard = ctrl.board
      ctrl.undo()
      ctrl.redo()
      ctrl.board should equal(changedBoard)
    }
  }
  "setupPlayers" should {
    "setup the amount of human players" in {
      c.setupPlayers("0")
      c.players.count(o => o.isBot) should be(2)
      c.setupPlayers("2")
      c.players.count(o => o.isBot) should be(0)
    }
  }
  "mapToBoard" should {
    "take a string and return a tuple of Ints" in {
      c.mapToBoard("a1") should be(0, 0)
    }
  }
  "nextPlayer" should {
    "return the player who's next" in {
      c.player = c.players(0)
      c.nextPlayer should be(c.players(1))
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      c.newGame()
      c.highlight()
      c.board.isHighlighted should be(true)
      c.board.valueOf(3, 2) should be(-1)
    }
    "de-highlight settable squares if already highlighted" in {
      c.highlight()
      c.board.isHighlighted should be(false)
    }
  }
  "boardToString" should {
    "represent the board" in {
      c.boardToString should equal(c.board.toString)
    }
  }
  "createBoard" should {
    "create a new instance of a game with the given dimensions" in {
      c.createBoard(8)
      c.board should be((new CreateBoardStrategy).createNewBoard(8))
      c.board.size should be (8)
      c.board.count._1 + c.board.count._2 should be (4)
    }
  }
  "resizeBoard" should {
    "increase the board size by 2 on input +" in {
      val c = new Controller
      val size = c.board.size
      c.resizeBoard("+")
      c.board.size should equal(size + 2)
    }
    "reduce the board size by 2 on input -" in {
      val c = new Controller
      val size = c.board.size
      c.resizeBoard("-")
      c.board.size should equal(size - 2)
    }
    "not do anything on input - if the board size is 4x4" in {
      val c = new Controller
      c.createBoard(4)
      val size = c.board.size
      c.resizeBoard("-")
      c.board.size should equal(size)
    }
    "reset the board size to 8 on input ." in {
      val c = new Controller(16)
      c.board.size should equal(16)
      c.resizeBoard(".")
      c.board.size should equal(8)
    }
  }
  "suggestions" should {
    "show possible moves" in {
      c.createBoard(8)
      c.player = c.players(0)
      c.suggestions should equal("C4 D3 E6 F5")
    }
  }
}
