package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  val player = new Player("Player", 1, Game(new Board))

  "setByOpp" should {
    "Be true if set by opponent" in {
      player.setByOpp(4, 4) should be (true)
    }
    "when not set be false" in {
      player.setByOpp(0, 0) should be (false)
    }
    "when set by Player be false " in {
      player.setByOpp(3, 4) should be (false)
    }
  }
  "setByPl" should {
    "Be false if set by opponent" in {
      player.setByPl(4, 4) should be (false)
    }
    "when not set be false" in {
      player.setByPl(0, 0) should be (false)
    }
    "when set by Player be true " in {
      player.setByPl(3, 4) should be (true)
    }
  }
  "moves" should {
    "return an a list of all possible moves " in {
      player.moves() shouldBe a [Map[_, _]]
    }
  }
  "highlight " should {
    "set highlighted cells to" in {
      player.highlight()
      player.board.field(2)(3).toString should be ("x ")
    }
  }
  "set" should {
    "return true if the move is possible" in {
      player.set(2, 3) should be (true)
    }
    "return false if the move is not possible" in {
      player.set(0, 0) should be (false)
    }
  }
  "checkMoves._2" should {
    "be empty if there is no valid move for the tile" in {
      player.checkMoves(0, 0)._2 shouldBe empty
    }
    "not be empty if there are valid moves for the tile" in {
      player.checkMoves(4,3)._2 should not be empty
    }
  }
  "checkRec" should {
    "return a tuple with values between 0 and 7 if there is a valid move" in {
      player.checkRec(3, 4, (1, 0)) should be (5, 4)
    }
    "return (-1, -1) if there is no valid move in this direction" in {
      player.checkRec(0, 0, (-1, 0)) should be (-1, -1)
    }
  }
}
