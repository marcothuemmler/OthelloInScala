package de.htwg.se.othello

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class UserModuleHttpServiceSpec extends AnyFreeSpec with Matchers with ScalatestRouteTest with UserModuleHttpService {

  val controller = new UserController

  "The UserModuleHttpService should support" - {
    "getting a basic HTML title in the '/usermodule' path" in {
      Get("/usermodule").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        responseAs[String] should equal("<h1>HTWG Othello</h1>")
      }
    }
    "returning the next player" in {
      Get("/usermodule/nextplayer").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "returning the amount of human players" in {
      Get("/usermodule/playercount").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "setting the amount of human players" in {
      Post("/usermodule/setupplayers/1").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "resetting the current player" in {
      Post("/usermodule/resetplayer").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "getting the current player as JsValue" in {
      Get("/usermodule/getcurrentplayer").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "selecting a player and returning a json representation" in {
      Get("/usermodule/getplayer/?isfirstplayer=false").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "returning all players as JsObject" in {
      Get("/usermodule/getplayers").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "setting the current player as human player" in {
      Post("/usermodule/setcurrentplayer/?name=Otto&value=1&isBot=false").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
    "setting the current player as bot" in {
      Post("/usermodule/setcurrentplayer/?name=Zuckerberg&value=2&isBot=true").~>(route)(TildeArrow.injectIntoRoute) ~> check {
        status.isSuccess() should be (true)
      }
    }
  }
}
