package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {

  val game = new Game
  val player = new Player("Otto", 1, game)
  val p = new Player(2, game)

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
        val moves = player.moves
      moves.keys should be (Set((3,4), (4,3)))
      moves.values should not be empty
    }
    "be empty if there are no valid moves" in {
      for (i <- 0 to 7) {
        game.flipLine((i, 0), (i, 7), 0)
      }
      val moves = player.moves
      moves.keys should be(empty)
      moves.values should be(empty)
      game.flip(3,3,2)
      game.flip(4,4,2)
      game.flip(3,4,1)
      game.flip(4,3,1)
    }
  }
  "count "should {
    "return the amount of tiles set by player" in {
      player.count should be(2)
    }
  }
  "highlight " should {
    "flip settable cells to -1" in {
      player.highlight()
      game.valueOf(2, 3) should be(-1)
    }
  }
  "set" should {
    "return true if the move is possible and the tile was set" in {
      player.set(2, 3) should be(true)
    }
    "return false if the move is not possible" in {
      player.set(0, 0) should be(false)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      player.getMoves(0, 0) should be((0, 0), List())
    }
    "return the checked square and a list with possible moves" in {
      player.getMoves(4, 3) should be(((4, 3), List((4,5))))
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
