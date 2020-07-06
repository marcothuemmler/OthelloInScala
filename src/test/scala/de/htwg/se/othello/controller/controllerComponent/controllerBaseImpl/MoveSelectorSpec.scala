package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.controller.controllerComponent.controllerMockImpl.MockController
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MoveSelectorSpec extends AnyWordSpec with Matchers {

  implicit val ctrl: MockController = new MockController

  "select" should {
    "always randomly select a move if the board size is not 8x8 or if the game has just started" in {
      ctrl.createBoard(4)
      val moveSelector = new EasyBot
      val selection = moveSelector.selection
      println(selection)
      (0 to 3) should contain(selection._1)
      (0 to 3) should contain(selection._2)
    }
  }
  "An EasyBot" should {
    "select a bad move using the search algorithm if the board size is 8x8" in {
      ctrl.createBoard(8)
      val moveSelector = new EasyBot
      ctrl.board = ctrl.board.flipLine((4, 3),(2, 3))(1)
      ctrl.board.flipLine((2, 4), (2, 4))(2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5))(1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4))(2)
      val selection = moveSelector.selection
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
  "A MediumBot" should {
    "select a move using the search algorithm if the board size is 8x8" in {
      ctrl.createBoard(8)
      val moveSelector = new MediumBot
      ctrl.board = ctrl.board.flipLine((4, 3), (2, 3))(1)
      ctrl.board.flipLine((2, 4), (2, 4))(2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5))(1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4))(2)
      val selection = moveSelector.selection
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
  "A HardBot" should {
    "select a good move using the search algorithm if the board size is 8x8" in {
      ctrl.createBoard(8)
      val moveSelector = new HardBot
      ctrl.board = ctrl.board.flipLine((4, 3), (2, 3))(1)
      ctrl.board.flipLine((2, 4), (2, 4))(2)
      ctrl.board = ctrl.board.flipLine((4, 4), (4, 5))(1)
      ctrl.board = ctrl.board.flipLine((3, 4), (5, 4))(2)
      val selection = moveSelector.selection
      (0 to 7) should contain(selection._1)
      (0 to 7) should contain(selection._2)
    }
  }
}
