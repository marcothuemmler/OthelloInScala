package de.htwg.se.othello.aview


import de.htwg.se.othello.controller.Controller
import de.htwg.se.othello.model.{Board, Player}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers{
  val players: Vector[Player] = Vector(new Player(1), new Player(2))
  val controller = new Controller(new Board, players)
  val tui = new Tui(controller )
  "A Tui " should {
    "do nothing on input q" in {
      tui.processInputLine("q")
    }
    "creat a new game on input n" in{
      tui.processInputLine("n")
      controller.board should be (new Board())
    }

    "get the highlight on input h" in{
      tui.processInputLine("h")
      controller.highlight() shouldBe a[String]


    }
    "get the suggestion on input s"in{
      tui.processInputLine("s")
      controller.suggestions shouldBe  a[String]
    }



  }

  "update" should {
    "update the borad" in{
      tui.update() shouldBe a[String]
    }
  }


}
