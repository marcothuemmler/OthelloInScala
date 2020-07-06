package de.htwg.se.othello.aview

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.htwg.se.othello.controller.controllerComponent.controllerMockImpl.MockController
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class OthelloHttpServiceSpec extends AnyFreeSpec with Matchers with ScalatestRouteTest with OthelloHttpService {

  val controller = new MockController

  "The OthelloHttpService should support" - {
    "returning a HTML representation of the board in the '/othello' path" in {
      Get("/othello") ~> route ~> check {
        responseAs[String] should equal("<h1>HTWG Othello</h1>" + controller.boardToHtml)
      }
    }
    "starting a new game" in {
      Post("/othello/new") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "undoing a move" in {
      Post("/othello/undo") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "redoing a move" in {
      Post("/othello/redo") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "highlighting valid moves on the board" in {
      Post("/othello/highlight") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "saving the game" in {
      Post("/othello/save") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "loading the game" in {
      Post("/othello/load") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "increasing the board size" in {
      Post("/othello/board/increase") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "reducing the board size" in {
      Post("/othello/board/reduce") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "resetting the board size" in {
      Post("/othello/board/reset") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "setting a square" in {
      Post("/othello/c4") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "setting the difficulty to easy" in {
      Post("/othello/difficulty/e") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "setting the difficulty to normal" in {
      Post("/othello/difficulty/m") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "setting the difficulty to hard" in {
      Post("/othello/difficulty/d") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "changing the amount of players to 2" in {
      Post("/othello/players/2") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "changing the amount of players to 1" in {
      Post("/othello/players/1") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "changing the amount of players to 0" in {
      Post("/othello/players/0") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
    "ignoring a wrong input" in {
      Post("/othello/wronginput") ~> route ~> check {
        status.isSuccess() should equal(true)
      }
    }
  }
}
