//package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl
//
//import de.htwg.se.othello.controller.controllerComponent.GameStatus
//import de.htwg.se.othello.model.boardComponent.BoardInterface
//import de.htwg.se.othello.model.boardComponent.boardBaseImpl.{Board, CreateBoardStrategy, Square}
//import org.scalatest.{Matchers, WordSpec}
//
//class ControllerSpec extends WordSpec with Matchers {
//  val controller = new Controller
//  controller.setupPlayers("2")
//  val b: BoardInterface = (new CreateBoardStrategy).createNewBoard(8)
//
//  "newGame" should {
//    "reset the board" in {
//      controller.newGame
//      controller.boardController.board should equal(b)
//      controller.getCurrentPlayer should be(controller.getPlayer(true))
//    }
//  }
//  "save and restore the whole game" in {
//    val board = controller.boardController.board
//    val player = controller.getCurrentPlayer
//    val difficulty = controller.difficulty
//    controller.save()
//    controller.difficulty = "Hard"
//    controller.setCurrentPlayer(controller.getPlayer(false))
//    controller.boardController.board = controller.boardController.board.flipLine((0, 0), (0, 0),1)
//    controller.boardController.board should not be board
//    controller.getCurrentPlayer should not be player
//    controller.difficulty should not be difficulty
//    controller.load()
//    controller.boardController.board should be(board)
//    controller.getCurrentPlayer should be(player)
//    controller.difficulty should be(difficulty)
//  }
//  "playerCount" should {
//    "count the amount of human players" in {
//      val ctrl = new Controller
//      ctrl.setupPlayers("1")
//      ctrl.playerCount should be(1)
//      ctrl.setupPlayers("0")
//      ctrl.playerCount should be(0)
//      ctrl.setupPlayers("2")
//      ctrl.playerCount should be(2)
//    }
//  }
//  "setDifficulty" should {
//    "set the difficulty of the game" in {
//      val ctrl = new Controller
//      ctrl.setDifficulty("e")
//      ctrl.difficulty should be("Easy")
//      ctrl.moveSelector shouldBe an[EasyBot]
//      ctrl.setDifficulty("m")
//      ctrl.difficulty should be("Normal")
//      ctrl.moveSelector shouldBe a[MediumBot]
//      ctrl.setDifficulty("d")
//      ctrl.difficulty should be("Hard")
//      ctrl.moveSelector shouldBe a[HardBot]
//    }
//  }
//  "set" should {
//    "set one disk and flip at least one of the opponents disks" in {
//      controller.set(2, 3)
//      controller.count(1) should be(4)
//      controller.count(2) should be(1)
//    }
//    "highlight possible moves on the board if the input is incorrect" in {
//      controller.newGame
//      controller.set(0, 0)
//      controller.boardController.board should equal(b.changeHighlight(1))
//    }
//    "omit a player who doesn't have valid moves" in {
//      val ctrl = new Controller
//      ctrl.boardController.board = Board(Vector.fill(8, 8)(Square(1)))
//        .flipLine((0, 3), (0, 6), 2)
//        .flipLine((6, 0), (6, 1), 2)
//        .flipLine((0, 7), (0, 7), 0)
//        .flipLine((7, 0), (7, 0), 0)
//      val currentPlayer = ctrl.getCurrentPlayer
//      ctrl.set(7, 0)
//      ctrl.getCurrentPlayer should equal(currentPlayer)
//    }
//  }
//  "selectAndSet" should {
//    "set a square" in {
//      val ctrl = new Controller
//      ctrl.setupPlayers("1")
//      ctrl.newGame
//      ctrl.setCurrentPlayer(ctrl.nextPlayer)
//      val board = ctrl.boardController.board
//      ctrl.selectAndSet()
//      ctrl.boardController.board should not equal board
//    }
//    "not change any square on the board if the player has no valid move" in {
//      controller.setupPlayers("1")
//      controller.boardController.board = new Board(8).flipLine((0, 7), (5, 7), 1)
//        .flipLine((0, 6), (0, 5), 1)
//        .flipLine((1, 6), (3, 6), 2)
//      val cBoard = controller.boardController.board
//      controller.setCurrentPlayer(controller.getPlayer(true))
//      controller.selectAndSet()
//      controller.boardController.board should equal(cBoard)
//    }
//  }
//  "omitPlayer" should {
//    "change the current player and set the gameStatus to omitted" in {
//      controller.newGame
//      val currentPlayer = controller.getCurrentPlayer
//      controller.omitPlayer()
//      controller.getCurrentPlayer should not equal currentPlayer
//      controller.gameStatus should equal(GameStatus.IDLE)
//    }
//  }
//  val ctrl = new Controller
//  ctrl.setupPlayers("2")
//  var changedBoard: BoardInterface = b
//  "undo" should {
//    "revert the board to a previous state" in {
//      ctrl.newGame
//      ctrl.set(3, 2)
//      changedBoard = ctrl.boardController.board
//      ctrl.undo()
//      ctrl.boardController.board should equal(b)
//    }
//  }
//  "redo" should {
//    "redo undone changes" in {
//      ctrl.redo()
//      ctrl.boardController.board should equal(changedBoard)
//    }
//  }
//  "canUndo" should {
//    val ctrl = new Controller
//    ctrl.setupPlayers("2")
//    "be false on a new game" in {
//      ctrl.canUndo should be(false)
//    }
//    "be true if there are moves to be undone" in {
//      ctrl.set(2, 3)
//      ctrl.canUndo should be (true)
//    }
//  }
//  "canRedo" should {
//    val ctrl = new Controller
//    ctrl.setupPlayers("2")
//    "be false on a new game" in {
//      ctrl.canUndo should be(false)
//    }
//    "be true if there are moves to be redone" in {
//      ctrl.set(2, 3)
//      ctrl.undo()
//      ctrl.canRedo should be (true)
//    }
//  }
//  "setupPlayers" should {
//    "setup the amount of human players" in {
//      controller.setupPlayers("0")
//      controller.playerCount should be(0)
//      controller.setupPlayers("2")
//      controller.playerCount should be(2)
//    }
//  }
//  "nextPlayer" should {
//    "return the player who's next" in {
//      controller.setCurrentPlayer(controller.getPlayer(true))
//      controller.nextPlayer should be(controller.getPlayer(false))
//    }
//  }
//  "highlight " should {
//    "highlight settable squares" in {
//      controller.newGame
//      controller.highlight()
//      controller.boardController.board.valueOf(3, 2) should be(-1)
//    }
//    "de-highlight settable squares if already highlighted" in {
//      controller.highlight()
//      controller.count(-1) should be(0)
//    }
//  }
//  "boardToString" should {
//    "represent the board" in {
//      controller.boardToString should equal(controller.boardController.board.toString)
//    }
//  }
//  "createBoard" should {
//    "create a new instance of a game with the given dimensions" in {
//      controller.createBoard(8)
//      controller.boardController.board should be(b)
//      controller.size should be(8)
//      controller.count(1) + controller.boardController.board.count(2) should be(4)
//    }
//  }
//  "resizeBoard" should {
//    "increase the board size by 2 on input +" in {
//      val size = controller.size
//      controller.resizeBoard("+")
//      controller.size should equal(size + 2)
//    }
//    "reduce the board size by 2 on input -" in {
//      val size = controller.size
//      controller.resizeBoard("-")
//      controller.size should equal(size - 2)
//    }
//    "not do anything on input - if the board size is 4x4" in {
//      controller.createBoard(4)
//      val size = controller.size
//      controller.resizeBoard("-")
//      controller.size should equal(size)
//    }
//    "reset the board size to 8 on input ." in {
//      controller.createBoard(16)
//      controller.size should equal(16)
//      controller.resizeBoard(".")
//      controller.size should equal(8)
//    }
//    "not do anything on input . if the board size is already 8x8" in {
//      controller.createBoard(8)
//      val size = controller.size
//      controller.resizeBoard(".")
//      controller.size should equal(size)
//    }
//  }
//  "suggestions" should {
//    "show possible moves" in {
//      controller.suggestions should equal("C4 D3 E6 F5")
//    }
//  }
//  "valueOf" should {
//    "return the value of the square" in {
//      controller.valueOf(0, 0) should be(0)
//      controller.valueOf(4, 3) should be(1)
//      controller.valueOf(3, 3) should be(2)
//    }
//  }
//  "score" should {
//    "be a draw if the amount of tiles is equal" in {
//      controller.score should be("Draw. 2:2")
//    }
//    "declare the Black player as winner if there are more black disks" in {
//      controller.newGame
//      controller.set(2, 3)
//      controller.score should be(s"Black wins by 4:1!")
//    }
//    "declare the White player as  winner if there are more white disks" in {
//      controller.newGame
//      controller.setCurrentPlayer(controller.getPlayer(false))
//      controller.set(4, 2)
//      controller.score should be(s"White wins by 4:1!")
//    }
//  }
//}
