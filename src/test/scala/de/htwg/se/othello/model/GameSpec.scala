package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class GameSpec extends WordSpec with Matchers {
  val game = new Game

  "flipCell" should {
    "set the value of the cell to new value" in {
      game.flipCell(7, 7, 2)
      game.field(7)(7).value should be(2)
    }
    "the field should have the value" in {
      game.valueOf(3, 3) should be (2)
    }
    "set the value of the cell vertically" in {

      game.flipLine((5,4), (3,4), 1 )
      game.field(4)(4).value should be (1)
    }
    "set the value of the cell horizontally " in{

      game.flipLine((4,3),(4,5),1)
      game.field(4)(4).value should be (1)
    }
    "set the value from up-left to down-right" in {

      game.flipLine((2,2), (5,5),1)
      game.field(3)(3).value should be (1)
      game.field(4)(4).value should be (1)
    }
    "set the value from up-right to down-left" in{

      game.flipLine((2,5),(5,2),2)
      game.field(4)(3).value should be (2)
      game.field(3)(4).value should be (2)
    }
  }
  "toString" should {
    "give back a nice String representation of the game" in {
      game.toString shouldBe a [String]
    }
  }
}
