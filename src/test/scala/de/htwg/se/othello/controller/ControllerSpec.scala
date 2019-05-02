package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  var c = new Controller(new Board, players)

  "A controller created without board parameter" should {
    "have a default board" in {
      val ctrl = new Controller(players)
      ctrl.board should be (new Board)
    }
  }
  "mapToBoard" should {
    "take a string and return a tuple of Ints" in {
      c.mapToBoard("a1") should be(0, 0)
    }
  }
  "switchPlayer" should {
    "switch the player" in {
      c.switchPlayer()
      c.player should be(c.p(1))
    }
  }
  "setByOpp" should {
    "be true if set by opponent" in {
      c.newGame()
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
      c.newGame()
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
        Map((3, 4) -> Seq((3, 2), (5, 4)), (4, 3) -> Seq((2, 3), (4, 5))))
    }
    "be empty if there are no valid moves" in {
      for (i <- 0 to 7) {
        c.board = c.board.flipLine((i, 0), (i, 7), 0)
      }
      c.moves should be(Map())
      c.board = new Board
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      c.highlight()
      c.board.isHighlighted should be (true)
    }
    "de-highlight settable squares" in {
      c.highlight()
      c.board.isHighlighted should be (false)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      c.getMoves(0, 0) should be((0, 0), Seq())
    }
    "return the checked square and a list with possible moves" in {
      c.player = c.p(0)
      c.getMoves(4, 3) should be(((4, 3), Vector((2, 3),(4, 5))))
    }
  }
  "checkRec" should {
    "return a tuple with values between 0 and 7 if there is a valid move" in {
      c.checkRecursive(3, 4, (1, 0)) should be(5, 4)
    }
    "return (-1, -1) if there is no valid move in this direction" in {
      c.checkRecursive(0, 0, (-1, 0)) should be(-1, -1)
    }
  }
  "boardToString" should {
    "be a string if there are no Moves bout the game is not over" in {
      for (i <- 0 to 7) {
        c.board = c.board.flipLine((i, 0), (i, 7), 0)
      }
      c.board = c.board.flip(0, 0,1)
      c.board = c.board.flip(0, 1,2)
      c.player = c.p(1)
      c.boardToString shouldBe a[String]
      c.newGame()
    }
    "show the board and ask for new game if the game is over" in {
      for (i <- 0 to 7) {
        c.board = c.board.flipLine((i, 0), (i, 7), 1)
      }
      c.boardToString should be (c.board.toString + "\n" + c.score +
        "\n\nPress \"n\" for new game")
      c.newGame()
    }
    "show suggestions and the board if the last move was not legal" in {
      c.notLegal = true
      c.boardToString should be (s"Valid moves for ${c.player}: " +
        s"${c.suggestions}\n${c.board.toString}")
      c.newGame()
    }
  }
  "score" should {
    "be a draw if the amount of tiles is equal" in {
      c.newGame()
      c.score should be ("Draw. 2:2")
    }
    "declare the winner if the amount of tiles is not equal" in {
      c.newGame()
      c.set(2,3)
      c.score should be ("Player1 wins by 4:1!")
    }
  }
  "botSet" should {
    "select a random valid square and set a disk there" in {
      c.newGame()
      val board = c.board
      c.botSet()
      board should not equal c.board
    }
    "not do anything but notifyObservers if there are no legal moves" in {
      c.newGame()
      c.board = c.board.flip(4,3,2)
      c.board = c.board.flip(3,4,2)
      val board = c.board
      c.botSet()
      board should equal(c.board)
    }
  }
}
