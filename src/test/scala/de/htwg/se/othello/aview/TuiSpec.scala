package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.{Board, CreateBoardStrategy, Square}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
  val controller = new Controller
  controller.setupPlayers("2")
  val tui = new Tui(controller)
  "A Tui " should {
    "set the amount of human players to 0 on input 0" in {
      tui.processInputLine("0")
      controller.players.count(o => o.isBot) should be(2)
    }
    "set the amount of human players to 1 on input 1" in {
      tui.processInputLine("1")
      controller.players.count(o => o.isBot) should be(1)
    }
    "set the amount of human players to 2 on input 2" in {
      tui.processInputLine("2")
      controller.players.count(o => o.isBot) should be(0)
    }
    "print suggestions on input s" in {
      tui.processInputLine("s")
    }
    "create a new game on input n" in {
      tui.processInputLine("n")
      controller.board should be((new CreateBoardStrategy).createNewBoard(8))
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
      val board = controller.board
      tui.processInputLine("a12")
      controller.board should equal(board)
    }
    val ctrl = new Controller
    ctrl.setupPlayers("2")
    val t = new Tui(ctrl)
    var changedBoard = ctrl.board
    "undo a step on input z" in {
      ctrl.createBoard(8)
      t.processInputLine("c4")
      t.processInputLine("c5")
      changedBoard = ctrl.board
      t.processInputLine("z")
      ctrl.board should equal((new CreateBoardStrategy).createNewBoard(8))
    }
    "redo a step on input y" in {
      t.processInputLine("y")
      ctrl.board should equal(changedBoard)
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
      ctrl.difficulty should be(1)
    }
    "set the difficulty of the bot to normal on input m" in {
      t.processInputLine("m")
      ctrl.difficulty should be(2)
    }
    "set the difficulty of the bot to hard on input d" in {
      t.processInputLine("d")
      ctrl.difficulty should be(3)
    }
  }
  "update" should {
    "print the current board and the score if the game is over" in {
      val ctrl = new Controller
      ctrl.board = Board(Vector.fill(8, 8)(Square(1)))
      new Tui(ctrl).update
    }
    "print the gameStatus and the current board if the game is not over " in {
      controller.newGame
    }
  }
  "save and restore the whole game" in {
    val board = controller.board
    val player = controller.player
    val difficulty = controller.difficulty
    tui.processInputLine("f")
    controller.difficulty = 3
    controller.player = controller.players(1)
    controller.board = controller.board.flipLine((0, 0), (0, 0),1)
    controller.board should not be board
    controller.player should not be player
    controller.difficulty should not be difficulty
    tui.processInputLine("l")
    controller.board should be(board)
    controller.player should be(player)
    controller.difficulty should be(difficulty)
  }
}
