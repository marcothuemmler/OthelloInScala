package de.htwg.se.othello.controller

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.model.{Board, Bot, Player, Square}
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  var c = new Controller(new Board, players)

  "A controller created without board parameter" should {
    "have a default board" in {
      val ctrl = new Controller(players)
      ctrl.board should be(new Board)
    }
  }
  "newGame" should {
    "reset the board" in {
      c.newGame()
      c.board should be(new Board())
      c.player should be(c.p(0))
    }
    "reset the board and make the first move if the first player ist a Bot" in {
      val ctrl = new Controller(Vector(new Bot(1), new Player(2)))
      ctrl.newGame()
      ctrl.board should not equal new Board()
      ctrl.player should not be ctrl.p(0)
    }
  }
  "set" should {
    "set one disk and flip at least one of the opponents disks" in {
      c.newGame()
      c.set(2, 3)
      c.board.countAll(1, 2) should be(4, 1)
    }
    "not change any square on the board if the input is incorrect" in {
      c.newGame()
      c.set(0, 0)
      c.board should equal(new Board)
    }
  }
  "setupPlayers" should {
    "setup the amount of human players" in {
      c.setupPlayers("0")
      c.p.count(o => o.isInstanceOf[Bot]) should be(2)
      c.setupPlayers("1")
      c.p.count(o => o.isInstanceOf[Bot]) should be(1)
      c.setupPlayers("2")
      c.p.count(o => o.isInstanceOf[Bot]) should be(0)
    }
  }
  "mapToBoard" should {
    "take a string and return a tuple of Ints" in {
      c.mapToBoard("a1") should be(0, 0)
    }
  }
  "nextPlayer" should {
    "return the player who's next" in {
      c.nextPlayer should be(c.p(1))
    }
  }
  "setByOpp" should {
    "be true if set by opponent" in {
      c.setByOpp(4, 4) should be(true)
    }
    "be false if not set" in {
      c.setByOpp(0, 0) should be(false)
    }
    "be false if set by Player" in {
      c.setByOpp(3, 4) should be(false)
    }
  }
  "setByPl" should {
    "be false if set by opponent" in {
      c.setByPl(4, 4) should be(false)
    }
    "be false if not set" in {
      c.setByPl(0, 0) should be(false)
    }
    "be true if set by Player " in {
      c.setByPl(3, 4) should be(true)
    }
  }
  "moves" should {
    "not be empty if there are valid moves" in {
      c.newGame()
      c.moves should be(
        Map((3, 4) -> Seq((3, 2), (5, 4)), (4, 3) -> Seq((2, 3), (4, 5)))
      )
    }
    "be empty if there are no valid moves" in {
      c.board = Board(Vector.fill(8, 8)(Square(0)))
      c.moves should be(Map())
      c.board = new Board
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      c.highlight()
      c.board.isHighlighted should be(true)
      c.board.valueOf(3,2) should be(-1)
    }
    "de-highlight settable squares if already highlighted" in {
      c.highlight()
      c.board.isHighlighted should be(false)
      c.board.count(-1) should be(0)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      c.getMoves(0, 0) should be((0, 0), Seq())
    }
    "return the checked square and a list with possible moves" in {
      c.player = c.p(0)
      c.getMoves(4, 3) should be(((4, 3), Vector((2, 3), (4, 5))))
    }
  }
  "checkRecursive" should {
    "return a tuple with values between 0 and 7 if there is a valid move" in {
      c.checkRecursive(3, 4, (1, 0)) should be(5, 4)
    }
    "return (-1, -1) if there is no valid move in this direction" in {
      c.checkRecursive(0, 0, (-1, 0)) should be(-1, -1)
    }
  }
  "status" should {
    "be a string if there are no Moves but the game is not over" in {
      for (i <- 0 to 7) {
        c.board = c.board.flipLine((i, 0), (i, 7), 0)
      }
      c.board = c.board.flip(0, 0, 1)
      c.board = c.board.flip(0, 1, 2)
      c.player = c.p(1)
      c.status should be(f"No valid moves for ${c.p(1)}. ${c.p(0)}'s turn.\n" +
        c.board.toString)
    }
    "show the board and ask for new game if the game is over" in {
      for (i <- 0 to 7) {
        c.board = c.board.flipLine((i, 0), (i, 7), 1)
      }
      c.status should be(c.board.toString + c.score +
        "\n\nPress \"n\" for new game")
      c.newGame()
    }
    "show suggestions and the board if the last move was not legal" in {
      c.moveIsLegal = false
      c.status should be(s"${c.suggestions}\n${c.board.toString}")
      c.newGame()
    }
    "print just the current board otherwise" in {
      c.newGame()
      c.status should equal(c.board.toString)
    }
  }
  "score" should {
    "be a draw if the amount of tiles is equal" in {
      c.newGame()
      c.score should be("Draw. 2:2")
    }
    "declare the winner if the amount of tiles is not equal" in {
      c.newGame()
      c.set(2, 3)
      c.score should be(s"${c.p(0)} wins by 4:1!")
    }
  }
  "select" should {
    "select a random valid move" in {
      val selection = c.select.get
      (0 to 7) should contain (selection._1)
      (0 to 7) should contain (selection._2)
    }
    "be None if there are no moves" in {
      c.newGame()
      c.p = Vector(new Bot(1), new Bot(2))
      c.board = c.board.flip(3, 3, 1)
      c.board = c.board.flip(4, 4, 1)
      c.select should be(None)
    }
  }
  "setAndNext" should {
    "set a square and skip the opponent as long as he can't make legal moves" in {
      val players: Vector[Player] = Vector(new Player(1), new Bot(2))
      val controller = new Controller(players)
      val tui = new Tui(controller)
      tui.update()
      controller.board = Board(Vector.tabulate(8, 8)((i, j) => {
        if (j == 7 || (j == 6 && i == 0)) Square(1) else Square(0)
      }))
      controller.board = controller.board.flipLine((1, 4), (1, 6), 2)
      controller.board = controller.board.flipLine((2, 5), (2, 6), 2)
      controller.board = controller.board.flip(3, 6, 2)
      controller.setAndNext(0, 5)
      controller.setAndNext(0, 4)
      controller.setAndNext(0, 3)
      controller.board.grid.flatten.count(s => s == Square(2)) should be(0)
    }
    "set a square and if the next player is a bot let it make a legal move" in {
      val players: Vector[Player] = Vector(new Player(1), new Bot(2))
      val controller = new Controller(players)
      controller.setAndNext(2, 3)
      controller.board.grid.flatten.count(s => s == Square(2)) should be > 1
    }
    "just switch and notifyObservers if no legal move exists" in {
      c.p = Vector(new Player(1), new Player(2))
      c.newGame()
      c.board = Board(Vector.tabulate(8, 8)((_, j) => {
        if (j == 7) Square(2) else Square(0)
      }))
      c.board = c.board.flip(0, 6, 1)
      val cTui = new Tui(c)
      cTui.processInputLine("q")
      val oldBoard = c.board
      c.setAndNext(0, 5)
      c.board should equal(oldBoard)
    }
    "take the input and let the player try again if the input was invalid" in {
      c.p = Vector(new Player(1), new Player(2))
      c.newGame()
      c.setAndNext(0, 0)
      c.player should be(c.p(0))
    }
  }
}
