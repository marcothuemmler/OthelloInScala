package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers{
  val board = new Board
  "the Board ist the playfield of Othello" when {
    "the field be initialized" in {
      board.field(3)(3).value should be (2)
      board.field(3)(4).value should be (1)
      board.field(1)(1).value should be (0)

    }
    "the field should have the value" in {
      board.valueOf(3, 3) should be (2)

    }
    "the board should be printed" in  {

      board.toString shouldBe a [String]


    }

  }

}
