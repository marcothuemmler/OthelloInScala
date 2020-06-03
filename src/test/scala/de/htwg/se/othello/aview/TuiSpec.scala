package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.controllerComponent.GameStatus
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
  val controller = new Controller
  controller.setupPlayers("2")
  val tui = new Tui(controller)
  "A Tui " should {
    "set the amount of human players to 0 on input 0" in {
      tui.processInputLine("0")
      controller.playerCount should be(2)
    }
    "set the amount of human players to 1 on input 1" in {
      tui.processInputLine("1")
      controller.playerCount should be(1)
    }
    "set the amount of human players to 2 on input 2" in {
      tui.processInputLine("2")
      controller.playerCount should be(2)
    }
    "print suggestions on input s" in {
      tui.processInputLine("s")
    }
    "create a new game on input n" in {
      tui.processInputLine("n")
      controller.boardController.board should be((new CreateBoardStrategy).createNewBoard(8))
    }
    "highlight possible moves on the board on input h" in {
      tui.processInputLine("h")
      controller.count(-1) should be > 0
    }
    "set a square and flip a disk on input c4" in {
      tui.processInputLine("c4")
      controller.valueOf(2, 3) should be(1)
      controller.valueOf(3, 3) should be(1)
    }
    "not set a square and not flip any disk on input a12" in {
      val board = controller.boardController.board
      tui.processInputLine("a12")
      controller.boardController.board should equal(board)
    }
    val ctrl = new Controller
    ctrl.setupPlayers("2")
    val t = new Tui(ctrl)
    var changedBoard = ctrl.boardController.board
    "undo a step on input z" in {
      ctrl.newGame
      t.processInputLine("c4")
      changedBoard = ctrl.boardController.board
      t.processInputLine("z")
      ctrl.boardController.board should equal((new CreateBoardStrategy).createNewBoard(8))
    }
    "redo a step on input y" in {
      t.processInputLine("y")
      ctrl.boardController.board should equal(changedBoard)
    }
    "resize the board on input +" in {
      val size = ctrl.size
      t.processInputLine("+")
      ctrl.size should be(size + 2)
    }
    "resize the board on input -" in {
      val size = ctrl.size
      t.processInputLine("-")
      ctrl.size should be(size - 2)
    }
    "reset the board size on input ." in {
      ctrl.createBoard(16)
      ctrl.size should equal(16)
      t.processInputLine(".")
      ctrl.size should be(8)
    }
    "set the difficulty of the bot to easy on input e" in {
      t.processInputLine("e")
      ctrl.difficulty should be("Easy")
    }
    "set the difficulty of the bot to normal on input m" in {
      t.processInputLine("m")
      ctrl.difficulty should be("Normal")
    }
    "set the difficulty of the bot to hard on input d" in {
      t.processInputLine("d")
      ctrl.difficulty should be("Hard")
    }
  }
  "update" should {
    "print the current board and the score if the gameStatus is GAME_OVER" in {
      val ctrl = new Controller
      ctrl.gameStatus = GameStatus.GAME_OVER
      new Tui(ctrl).update
    }
    "print only the current board if the gameStatus is IDLE" in {
      controller.newGame
    }
    "print the gameStatus, the omitted player and the board if the gameStatus is OMITTED" in {
      val ctrl = new Controller
      ctrl.gameStatus = GameStatus.OMITTED
      new Tui(ctrl).update
    }
    "print the gameStatus followed by suggestions if the gameStatus is ILLEGAL" in {
      val ctrl = new Controller
      ctrl.gameStatus = GameStatus.ILLEGAL
      new Tui(ctrl).update
    }
    "print the gameStatus followed by the board if the gameStatus is LOAD_SUCCESS" in {
      val ctrl = new Controller
      ctrl.gameStatus = GameStatus.LOAD_SUCCESS
      new Tui(ctrl).update
    }
    "print the gameStatus if the gameStatus is in any other state" in {
      val ctrl = new Controller
      val t = new Tui(ctrl)
      ctrl.gameStatus = GameStatus.LOAD_FAIL
      t.update
    }

  }
  "save and restore the whole game" in {
    val board = controller.boardController.board
    val player = controller.getCurrentPlayer
    val difficulty = controller.difficulty
    tui.processInputLine("f")
    controller.difficulty = "Hard"
    controller.setCurrentPlayer(controller.getPlayer(false))
    controller.boardController.board = controller.boardController.board.flipLine((0, 0), (0, 0),1)
    controller.boardController.board should not be board
    controller.getCurrentPlayer should not be player
    controller.difficulty should not be difficulty
    tui.processInputLine("l")
    controller.boardController.board should be(board)
    controller.getCurrentPlayer should be(player)
    controller.difficulty should be(difficulty)
  }
}
