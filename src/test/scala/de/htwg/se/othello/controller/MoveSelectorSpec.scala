package de.htwg.se.othello.controller

import org.scalatest.{Matchers, WordSpec}

class MoveSelectorSpec extends WordSpec with Matchers {
  "select" should {
    val ctrl = new Controller
    ctrl.createBoard(4)
    val moveSelector = new MoveSelector(ctrl)
    "randomly select a move if the board size is not 8x8" in {
      val selection = moveSelector.select().get
      (0 to 3) should contain(selection._1)
      (0 to 3) should contain(selection._2)
    }
    "select a move using the search algorithm if the board size is 8x8" in {
      ctrl.createBoard(8)
      val selection = moveSelector.select().get
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
}
