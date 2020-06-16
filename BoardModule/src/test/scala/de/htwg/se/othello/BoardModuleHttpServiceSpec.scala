package de.htwg.se.othello

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.BoardController
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json

class BoardModuleHttpServiceSpec extends AnyFreeSpec with Matchers with ScalatestRouteTest with BoardModuleHttpService {

  val controller = new BoardController

  "The boardModuleHttpService should support" - {
    "getting a basic HTML title in the root path" in {
      Get("/") ~> route ~> check {
        responseAs[String] should equal("<h1>HTWG Othello</h1>")
      }
    }
    "getting a basic HTML title in the '/boardmodule' path" in {
      Get("/boardmodule") ~> route ~> check {
        responseAs[String] should equal("<h1>HTWG Othello</h1>")
      }
    }
    "returning the board size" in {
      Get("/boardmodule/size") ~> route ~> check {
        responseAs[String] should equal("8")
      }
    }
    "returning a json representation of the board" in {
      Get("/boardmodule/boardjson") ~> route ~> check {
        responseAs[String] should equal("" + controller.toJson)
      }
    }
    "returning a String representation of the board" in {
      Get("/boardmodule/boardstring") ~> route ~> check {
        responseAs[String] should equal(controller.boardToString)
      }
    }
    "returning a HTML representation of the board" in {
      Get("/boardmodule/boardhtml") ~> route ~> check {
        responseAs[String] should equal(controller.boardToHtml)
      }
    }
    "returning the curret game status" in {
      Get("/boardmodule/gameover") ~> route ~> check {
        responseAs[String].toBoolean should equal(false)
      }
    }
    "resizing the board" in {
      Post("/boardmodule/resize/?op=%2B") ~> route  ~> check {
        status.isSuccess() should be(true)
      }
    }
    "highlighting valid moves on the board" in {
      Post("/boardmodule/changehighlight/?value=1") ~> route ~> check {
        status.isSuccess() should be(true)
      }
    }
    "creating a board of a given size" in {
      Post("/boardmodule/create/?size=8") ~> route ~> check {
        status.isSuccess() should be(true)
      }
    }
    "returning the value of a given square on the board" in {
      controller.createBoard(8)
      Get("/boardmodule/valueof/?col=3&row=3") ~> route ~> check {
        responseAs[String] should be("2")
      }
    }
    "returning all the valid moves as Json" in {
      Get("/boardmodule/moves/?value=1") ~> route ~> check {
        val movesJson = Json.parse(responseAs[String])
        movesJson should equal(controller.movesToJson(1))
      }
    }
    "updating the board" in {
      Post("/boardmodule/set", controller.toJson.toString) ~> route ~> check {
        status.isSuccess() should be(true)
      }
    }
    "returning the amount of pieces of a given value" in {
      Get("/boardmodule/count/?value=1") ~> route ~> check {
        responseAs[String] should be("2")
      }
    }
  }
}
