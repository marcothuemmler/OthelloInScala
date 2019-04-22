package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  val board = new Board

  "flip" should {
    "set the value of the cell to new value" in {
      board.flip((7, 7), 2)
      board.grid(7)(7) should be(Square(2))
    }
  }
  "flipLine" should {
    "change the value of a connected line to the new value" in {
      board.flipLine((5, 4), (3, 4), 1)
      board.grid(5)(4).value should be(1)
      board.grid(4)(4).value should be(1)
      board.grid(3)(4).value should be(1)
    }
  }
  "toString" should {
    "return a nice String representation of the game" in {
      board.toString shouldBe a[String]
    }
  }
}
