package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Bot, Player, Square}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Failure

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  var c = new Controller(players)

  "A controller created without board parameter" should {
    "have a default board" in {
      val ctrl = new Controller(players)
      ctrl.board should equal(new Board)
    }
  }
  "newGame" should {
    "reset the board" in {
      c.set(2, 3)
      c.board should not equal new Board
      c.newGame()
      c.board should equal(new Board)
      c.player should be(c.players(0))
    }
    "reset the board and make the first move if the first player ist a Bot" in {
      val ctrl = new Controller(Vector(new Bot(1), new Player(2)))
      ctrl.newGame()
      ctrl.board should not equal new Board
      ctrl.player should not be ctrl.players(0)
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
      c.board should equal(new Board)
    }
    "skip the player if there are no valid moves to be made" in {
      c.board = Board(Vector.fill(8, 8)(Square(0)))
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
      c.board = Board(Vector.fill(8, 8)(Square(0)))
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
      ctrl.set(3, 2)
      ctrl.undo()
      ctrl.board should equal(new Board)
    }
  }
  "redo" should {
    "redo undone changes" in {
      val ctrl = new Controller
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
  /*
  "select" should {
    "select a random valid move" in {
      val selection = c.select.get
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
    "fail if there are no moves" in {
      c.newGame()
      c.board = c.board.flipLine((3, 3), (4, 4), 1)
      c.select shouldBe a[Failure[_]]
    }
  }
  */
  "suggestions" should {
    "show possible moves" in {
      c.board = new Board
      c.player = c.players(0)
      c.suggestions should equal("C4 D3 E6 F5")
    }
  }
}
