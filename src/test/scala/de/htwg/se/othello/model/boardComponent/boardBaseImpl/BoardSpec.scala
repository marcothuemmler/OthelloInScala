package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {
  val board: Board = (new CreateBoardStrategy).createNewBoard(8).asInstanceOf[Board]

  "setByOpp" should {
    "be true if set by opponent" in {
      board.setByOpp(1, 4, 4) should be(true)
    }
    "be false if not set" in {
      board.setByOpp(1, 0, 0) should be(false)
    }
    "be false if set by Player" in {
      board.setByOpp(1, 3, 4) should be(false)
    }
  }
  "moves" should {
    "not be empty if there are valid moves" in {
      board.moves(1) should be(
        Map((3, 4) -> Seq((3, 2), (5, 4)), (4, 3) -> Seq((2, 3), (4, 5)))
      )
    }
    "be empty if there are no valid moves" in {
      new Board(8).moves(1) should be(Map.empty)
    }
  }
  "getMoves" should {
    "return the checked square and an empty list if there are no valid moves" in {
      board.getMoves(1, 0, 0) should be((0, 0), Seq.empty)
    }
    "return the checked square and a list with possible moves" in {
      board.getMoves(1, 4, 3) should be(((4, 3), Vector((2, 3), (4, 5))))
    }
  }
  "checkRec" should {
    "be a tuple with values between 0 and 7 if there is a valid move" in {
      board.checkRec(1, 3, 4, (1, 0)) should be(Some(5, 4))
    }
    "be None if there is no valid move in this direction" in {
      board.checkRec(1, 0, 0, (-1, 0)) should be(None)
    }
  }
  "isSet" should {
    "be true if there is at least one disk on the board" in {
      new Board(8).isSet should be(false)
      board.isSet should be(true)
    }
  }
  "valueOf" should {
    "be the value of the square" in {
      board.valueOf(2, 2) should be(0)
    }
  }
  "highlight" should {
    "highlight possible moves on the board" in {
      board.highlight(1).valueOf(2, 3) should be(-1)
    }
  }
  "count" should {
    "count the disks of one player on the board" in {
      board.count(1) should be(2)
    }
  }
  "deHighlight" should {
    "remove all the highlights on the board" in {
      val b = board.highlight(1).deHighlight
      b.count(-1) should be(0)
    }
  }
  "flipLine" should {
    "change the value of a connected line to the new value" in {
      val b = board.flipLine((5, 4), (3, 4), 1)
      b.grid(5)(4).value should be(1)
      b.grid(4)(4).value should be(1)
      b.grid(3)(4).value should be(1)
    }
  }
  "toString" should {
    "return a nice String representation of the board" in {
      board.toString shouldBe a[String]
    }
  }
}
