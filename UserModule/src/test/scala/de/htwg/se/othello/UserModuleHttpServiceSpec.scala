package de.htwg.se.othello

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class UserModuleHttpServiceSpec extends AnyFreeSpec with Matchers with ScalatestRouteTest with UserModuleHttpService {

  val controller = new UserController

  "The UserModuleHttpService should support" - {
    "getting a basic HTML title in the '/usermodule' path" in {
      Get("/usermodule") ~> route ~> check {
        responseAs[String] should equal("<h1>HTWG Othello</h1>")
      }
    }
    "returning the next player" in {
      Get("/usermodule/nextplayer") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "returning the amount of human players" in {
      Get("/usermodule/playercount") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "setting the amount of human players" in {
      Post("/usermodule/setupplayers/1") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "resetting the current player" in {
      Post("/usermodule/resetplayer") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "getting the current player as JsValue" in {
      Get("/usermodule/getcurrentplayer") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "selecting a player and returning a json representation" in {
      Get("/usermodule/getplayer/?isfirstplayer=false") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "returning all players as JsObject" in {
      Get("/usermodule/getplayers") ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
    "setting the current player" in {
      Post("/usermodule/setcurrentplayer/", controller.playerToJson.toString) ~> route ~> check {
        status.isSuccess() should be (true)
      }
    }
  }
}
