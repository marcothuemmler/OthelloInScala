package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.controller.controllerComponent.GameStatus
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.{Board, CreateBoardStrategy, Square}
import de.htwg.se.othello.model.Player
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  val controller = new Controller(players)
  val b: BoardInterface = (new CreateBoardStrategy).createNewBoard(8)

  "A controller created without parameters" should {
    "have an empty board of size 8x8" in {
      (new Controller).board should equal(new Board)
    }
  }
  "newGame" should {
    "reset the board" in {
      controller.newGame
      controller.board should equal(b)
      controller.player should be(controller.players(0))
    }
  }
  "playerCount" should {
    "count the amount of human players" in {
      val ctrl = new Controller
      ctrl.playerCount should be(1)
      ctrl.setupPlayers("0")
      ctrl.playerCount should be(0)
      ctrl.setupPlayers("2")
      ctrl.playerCount should be(2)
    }
  }
  "setDifficulty" should {
    "set the difficulty of the game" in {
      val ctrl = new Controller
      ctrl.setDifficulty("e")
      ctrl.difficulty should be(1)
      ctrl.setDifficulty("m")
      ctrl.difficulty should be(2)
      ctrl.setDifficulty("d")
      ctrl.difficulty should be(3)
    }
  }
  "moveSelector" should {
    "return a new MoveSelector with the given difficulty" in {
      val ctrl = new Controller
      ctrl.moveSelector(1) shouldBe an[EasyBot]
      ctrl.moveSelector(2) shouldBe a[MediumBot]
      ctrl.moveSelector(3) shouldBe a[HardBot]
    }
  }
  "set" should {
    "set one disk and flip at least one of the opponents disks" in {
      controller.set(2, 3)
      controller.count(1) should be(4)
      controller.count(2) should be(1)
    }
    "not change any square on the board if the input is incorrect" in {
      controller.newGame
      controller.set(0, 0)
      controller.board should equal(b)
    }
    "omit a player who doesn't have valid moves" in {
      val ctrl = new Controller
      ctrl.board = Board(Vector.fill(8, 8)(Square(1)))
        .flipLine((0, 3), (0, 6), 2)
        .flipLine((6, 0), (6, 1), 2)
        .flipLine((0, 7), (0, 7), 0)
        .flipLine((7, 0), (7, 0), 0)
      val currentPlayer = ctrl.player
      ctrl.set(7, 0)
      ctrl.player should equal(currentPlayer)
    }
  }
  "selectAndSet" should {
    "not change any square on the board if the player has no valid move" in {
      controller.setupPlayers("1")
      controller.board = (new Board).flipLine((0, 7), (5, 7), 1)
        .flipLine((0, 6), (0, 5), 1)
        .flipLine((1, 6), (3, 6), 2)
      val cBoard = controller.board
      controller.player = controller.players(1)
      controller.selectAndSet()
      controller.board should equal(cBoard)
    }
  }
  "omitPlayer" should {
    "change the current player and set the gameStatus to omitted" in {
      val currentPlayer = controller.player
      controller.omitPlayer()
      controller.player should not equal currentPlayer
      controller.gameStatus should equal(GameStatus.OMITTED)
    }
  }
  val ctrl = new Controller(players)
  var changedBoard: BoardInterface = b
  "undo" should {
    "revert the board to a previous state" in {
      ctrl.createBoard(8)
      ctrl.set(3, 2)
      ctrl.set(4, 2)
      changedBoard = ctrl.board
      ctrl.undo()
      ctrl.board should equal(b)
    }
  }
  "redo" should {
    "redo undone changes" in {
      ctrl.redo()
      ctrl.board should equal(changedBoard)
    }
  }
  "setupPlayers" should {
    "setup the amount of human players" in {
      controller.setupPlayers("0")
      controller.players.count(o => o.isBot) should be(2)
      controller.setupPlayers("2")
      controller.players.count(o => o.isBot) should be(0)
    }
  }
  "nextPlayer" should {
    "return the player who's next" in {
      controller.player = controller.players(0)
      controller.nextPlayer should be(controller.players(1))
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      controller.newGame
      controller.highlight()
      controller.board.valueOf(3, 2) should be(-1)
    }
    "de-highlight settable squares if already highlighted" in {
      controller.highlight()
      controller.count(-1) should be(0)
    }
  }
  "boardToString" should {
    "represent the board" in {
      controller.boardToString should equal(controller.board.toString)
    }
  }
  "createBoard" should {
    "create a new instance of a game with the given dimensions" in {
      controller.createBoard(8)
      controller.board should be(b)
      controller.size should be(8)
      controller.count(1) + controller.board.count(2) should be(4)
    }
  }
  "resizeBoard" should {
    "increase the board size by 2 on input +" in {
      val size = controller.size
      controller.resizeBoard("+")
      controller.size should equal(size + 2)
    }
    "reduce the board size by 2 on input -" in {
      val size = controller.size
      controller.resizeBoard("-")
      controller.size should equal(size - 2)
    }
    "not do anything on input - if the board size is 4x4" in {
      controller.createBoard(4)
      val size = controller.size
      controller.resizeBoard("-")
      controller.size should equal(size)
    }
    "reset the board size to 8 on input ." in {
      controller.createBoard(16)
      controller.size should equal(16)
      controller.resizeBoard(".")
      controller.size should equal(8)
    }
    "not do anything on input . if the board size is already 8x8" in {
      controller.createBoard(8)
      val size = controller.size
      controller.resizeBoard(".")
      controller.size should equal(size)
    }
  }
  "suggestions" should {
    "show possible moves" in {
      controller.player = controller.players(0)
      controller.suggestions should equal("C4 D3 E6 F5")
    }
  }
  "valueOf" should {
    "return the value of the square" in {
      controller.valueOf(0, 0) should be(0)
      controller.valueOf(4, 3) should be(1)
      controller.valueOf(3, 3) should be(2)
    }
  }
  "score" should {
    "be a draw if the amount of tiles is equal" in {
      controller.score should be("Draw. 2:2")
    }
    "declare the Black player as winner if there are more black disks" in {
      controller.newGame
      controller.set(2, 3)
      controller.score should be(s"Black wins by 4:1!")
    }
    "declare the White player as  winner if there are more white disks" in {
      controller.newGame
      controller.player = controller.players(1)
      controller.set(4, 2)
      controller.score should be(s"White wins by 4:1!")
    }
  }
}
