package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  var board = new Board

  "A board without parameters" should {
    "be a board with four squares set" in {
      val b = new Board
      b.count(1) should be (2)
      b.count(2) should be (2)
    }
  }

  "isSet" should {
    "be true if there is a disk" in {
      board.isSet(3,4) should be (true)
    }
    "be false if there is no disk" in {
      board.isSet(0,0) should be (false)
    }
  }

  "valueOf" should {
    "return the value of the square" in {
      board.valueOf(2, 2) should be (0)
    }
  }

  "highlight" should {
    "return the value of the square" in {
      val b = board.highlight(2, 2)
      b.valueOf(2, 2) should be (-1)
    }
  }

  "isHighlighted" should {
    "be true if there are highlighted squares" in {
      val b = board.highlight(2, 2)
      b.isHighlighted should be (true)
    }
  }

  "countAll" should {
    "count the disks of both players on the board" in {
      val b = new Board
      b.countAll(1, 2) should be (2, 2)
    }
  }

  "count" should {
    "count the disks of one player on the board" in {
      val b = new Board
      b.count(1) should be (2)
    }
  }

  "countHighlighted" should {
    "be true if there are highlighted squares" in {
      val b = board.highlight(2, 2)
      b.countHighlighted should be (1)
    }
  }

  "deHighlight" should {
    "remove all the highlights on the board" in {
      var b = new Board
      b = b.highlight(2,2)
      b = b.deHighlight
      b.isHighlighted should be (false)
    }
  }

  "flip" should {
    "set the value of the cell to new value" in {
      board = board.flip((7, 7), 2)
      board.grid(7)(7) should be(Square(2))
    }
  }
  "flipLine" should {
    "change the value of a connected line to the new value" in {
      board = board.flipLine((5, 4), (3, 4), 1)
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
