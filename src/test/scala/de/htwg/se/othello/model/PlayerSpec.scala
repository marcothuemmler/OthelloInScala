package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {

  val board = new Board
  val player = new Player("Otto", 1, board)
  val p = new Player(2, board)

  "A player" should {
    "have a toString method" in {
      player.toString should be("Otto")
    }
  }
  "A Player without name parameter" should {
    "have a default name" in {
      p.toString should be("Player2")
    }
  }
  "setByOpp" should {
    "Be true if set by opponent" in {
      player.setByOpp(4, 4) should be(true)
    }
    "be false if not set" in {
      player.setByOpp(0, 0) should be(false)
    }
    "be false if set by Player" in {
      player.setByOpp(3, 4) should be(false)
    }
  }
  "setByPl" should {
    "be false if set by opponent" in {
      player.setByPl(4, 4) should be(false)
    }
    "be false if not set" in {
      player.setByPl(0, 0) should be(false)
    }
    "be true if set by Player " in {
      player.setByPl(3, 4) should be(true)
    }
  }
  "moves" should {
    "not be empty if there are valid moves" in {
      player.moves should be(
        Map((3,4) -> Seq((3,2), (5,4)), (4,3) -> Seq((2,3), (4,5))))
    }
    "be empty if there are no valid moves" in {
      for (i <- 0 to 7) {
        board.flipLine((i, 0), (i, 7), 0)
      }
      player.moves should be(Map())
      board.flip((3, 3), 2)
      board.flip((4, 4), 2)
      board.flip((3, 4), 1)
      board.flip((4, 3), 1)
    }
  }
  "count " should {
    "return the amount of disks set by player" in {
      player.count should be(2)
    }
  }
  "highlight " should {
    "highlight settable squares" in {
      player.highlight()
      board.grid(2)(3).value should be(-1)
    }
  }
  "set" should {
    "return true if the move is possible and the disk was set" in {
      player.set(2, 3) should be(true)
    }
    "return false if the move is not possible" in {
      player.set(0, 0) should be(false)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      player.getMoves(0, 0) should be((0, 0), Seq())
    }
    "return the checked square and a list with possible moves" in {
      player.getMoves(4, 3) should be(((4, 3), Seq((4, 5))))
    }
  }
  "checkRec" should {
    "return a tuple with values between 0 and 7 if there is a valid move" in {
      player.checkRec(3, 4, (1, 0)) should be(5, 4)
    }
    "return (-1, -1) if there is no valid move in this direction" in {
      player.checkRec(0, 0, (-1, 0)) should be(-1, -1)
    }
  }
}
