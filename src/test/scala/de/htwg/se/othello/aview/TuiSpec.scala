package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.Controller
import de.htwg.se.othello.model.{Board, Bot, Player}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers{
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  val controller = new Controller(new Board, players)
  val tui = new Tui(controller)
  "A Tui " should {
    "do nothing on input q" in {
      tui.processInputLine("q")
    }
    "set the amount of human players to 0 on input 0" in {
      tui.processInputLine("0")
      controller.p.count(o => o.isInstanceOf[Bot]) should be (2)
    }
    "set the amount of human players to 1 on input 1" in {
      tui.processInputLine("1")
      controller.p.count(o => o.isInstanceOf[Bot]) should be (1)
    }
    "set the amount of human players to 2 on input 2" in {
      tui.processInputLine("2")
      controller.p.count(o => o.isInstanceOf[Bot]) should be (0)
    }
    "print suggestions on input s" in {
      tui.processInputLine("s")
    }
    "create a new game on input n" in {
      tui.processInputLine("n")
      controller.board should be (new Board())
    }
    "highlight possible moves on the board on input h" in {
      tui.processInputLine("h")
      controller.board.isHighlighted should be(true)
    }
    "set a square and flip a disk on input c4" in {
      tui.processInputLine("c4")
      controller.board.valueOf(2, 3) should be (1)
      controller.board.valueOf(3, 3) should be (1)
    }
    "not set a square and not flip any disk on input a12" in {
      val board = controller.board
      tui.processInputLine("a12")
      controller.board should equal(board)
    }
  }
}
