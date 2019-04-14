package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class GameSpec extends WordSpec with Matchers {
  var game = new Game

  "flipCell" should {
    "set the value of the cell to new value" in {
      game.flip(7, 7, 2)
      game.field(7)(7).value should be(2)
    }
    "the field should have the value" in {
      game.valueOf(3, 3) should be (2)
    }
    "set the value of the cell vertically" in {
      game.flipLine((5,4), (3,4), 1 )
      game.field(3)(4).value should be (1)
      game.field(4)(4).value should be (1)
      game.field(5)(4).value should be (1)
    }
    "set the value of the cell horizontally " in{
      game.flipLine((4,3),(4,5),1)
      game.field(4)(3).value should be (1)
      game.field(4)(4).value should be (1)
      game.field(4)(5).value should be (1)
    }
    "set the value from up-left to down-right" in {
      game.flipLine((2,2), (5,5),1)
      game.field(2)(2).value should be (1)
      game.field(3)(3).value should be (1)
      game.field(4)(4).value should be (1)
      game.field(5)(5).value should be (1)
    }
    "set the value from up-right to down-left" in{
      game.flipLine((2,5),(5,2),2)
      game.field(2)(5).value should be (2)
      game.field(3)(4).value should be (2)
      game.field(4)(3).value should be (2)
      game.field(5)(2).value should be (2)
    }
  }
  "isSet" should {
    "return true if there is a tile" in {
      game.isSet(4,3) should be(true)
      game.isSet(0,0) should be(false)
    }
  }
  "toString" should {
    "give back a nice String representation of the game" in {
      game.stringRep shouldBe a [String]
    }
  }
}
