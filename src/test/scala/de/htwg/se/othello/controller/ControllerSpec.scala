package de.htwg.se.othello.controller

import de.htwg.se.othello.model.{Board, Player}
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  var c = new Controller(new Board, players)
  "boardToString" should {
    "return a nice String representation of the board" in {
      c.boardToString shouldBe a[String]
    }
  }
  "mapToBoard" should {
    "return a tuple" in {
      c.mapToBoard("a1") should be(0, 0)
    }
  }
  "mapOutput" should {
    "take an Integer and give us back a String" in {
      c.mapOutput(0, 0) should be("A1")
    }
  }
  "changeCurrent" should {
    "change the value of i" in {
      c.switchPlayer should be(c.players(1))
    }
  }
  "setByOpp" should {
    "Be true if set by opponent" in {
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
  "count " should {
    "return the amount of disks set by player" in {
      c.count should be(2)
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      c.highlight()
      c.board.grid(2)(3).value should be(-1)
    }
  }
  "set" should {
    "return true if the move is possible and the disk was set" in {
      c.set("c4") should be(true)
    }
    "return false if the move is not possible" in {
      c.set("a1") should be(false)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      c.getMoves(0, 0) should be((0, 0), Seq())
    }
    "return the checked square and a list with possible moves" in {
      c.player = c.players(0)
      c.getMoves(4, 3) should be(((4, 3), Seq((4, 5))))
    }
  }
  "checkRec" should {
    "return a tuple with values between 0 and 7 if there is a valid move" in {
      c.checkRec(3, 4, (1, 0)) should be(5, 4)
    }
    "return (-1, -1) if there is no valid move in this direction" in {
      c.checkRec(0, 0, (-1, 0)) should be(-1, -1)
    }
  }
}
