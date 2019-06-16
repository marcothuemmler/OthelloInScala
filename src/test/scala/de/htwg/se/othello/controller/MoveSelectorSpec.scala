package de.htwg.se.othello.controller

import org.scalatest.{Matchers, WordSpec}

class MoveSelectorSpec extends WordSpec with Matchers {

  val ctrl = new Controller

  "select" should {
    "always randomly select a move if the board size is not 8x8 or if the game has just started" in {
      val moveSelector = new EasyBot(ctrl)
      ctrl.createBoard(4)
      val selection = moveSelector.select.get
      (0 to 3) should contain(selection._1)
      (0 to 3) should contain(selection._2)
    }
  }
  "An EasyBot" should {
    "select a bad move using the search algorithm if the board size is 8x8" in {
      val moveSelector = new EasyBot(ctrl)
      ctrl.createBoard(8)
      ctrl.board = ctrl.board.flipLine((4, 3), (2, 3), 1)
      ctrl.board = ctrl.board.flip(2, 4, 2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5), 1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4), 2)
      val selection = moveSelector.select.get
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
  "A MediumBot" should {
    "select a move using the search algorithm if the board size is 8x8" in {
      val moveSelector = new MediumBot(ctrl)
      ctrl.createBoard(8)
      ctrl.board = ctrl.board.flipLine((4, 3), (2, 3), 1)
      ctrl.board = ctrl.board.flip(2, 4, 2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5), 1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4), 2)
      val selection = moveSelector.select.get
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
  "A HardBot" should {
    "select a good move using the search algorithm if the board size is 8x8" in {
      val moveSelector = new HardBot(ctrl)
      ctrl.createBoard(8)
      ctrl.board = ctrl.board.flipLine((4, 3), (2, 3), 1)
      ctrl.board = ctrl.board.flip(2, 4, 2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5), 1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4), 2)
      val selection = moveSelector.select.get
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
}
