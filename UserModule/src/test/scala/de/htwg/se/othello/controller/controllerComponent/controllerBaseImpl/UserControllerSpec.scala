package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsObject, Json}

class UserControllerSpec extends AnyWordSpec with Matchers {

  "nextPlayer" should {
    "return the next player" in {
      val controller = new UserController
      val currentPlayer = controller.player
      controller.nextPlayer should not equal currentPlayer
    }
  }
  "playerCount" should {
    "return the amount of human players currently playing" in {
      (new UserController).playerCount should be(1)
    }
  }
  "setupPlayers" should {
    "set the amount of human players to 0 on input 0" in {
      val controller = new UserController
      controller.setupPlayers("0")
      controller.playerCount should be(0)
    }
    "set the amount of human players to 1 on input 1" in {
      val controller = new UserController
      controller.setupPlayers("1")
      controller.playerCount should be(1)
    }
    "set the amount of human players to 2 on input 2" in {
      val controller = new UserController
      controller.setupPlayers("2")
      controller.playerCount should be(2)
    }
  }
  "resetPlayer" should {
    "reset the current player" in {
      val controller = new UserController
      controller.resetPlayer()
      controller.player should equal(controller.players(0))
    }
  }

  "getPlayer" should {
    "select the first player if the given parameter is true" in {
      val controller = new UserController
      val currentPlayer = controller.getPlayer(true)
      controller.player should equal(currentPlayer)
    }
  }
  "getPlayer" should {
    "select the second player if the given parameter is false" in {
      val controller = new UserController
      val currentPlayer = controller.getPlayer(false)
      controller.player should not equal currentPlayer
    }
  }
  "setCurrentPlayer" should {
    "set the current player to the one passed as argument" in {
      val player = Player("John", 1)
      val controller = new UserController
      val previousPlayer = controller.player
      controller.setCurrentPlayer(player)
      controller.player should not equal previousPlayer
    }
  }
  "playerToJson" should {
    "return a Json representation of the player" in {
      val playerJson = (new UserController).playerToJson
      (playerJson \ "name").as[String] should be("Black")
      (playerJson \ "value").as[Int] should be(1)
      (playerJson \ "isBot").as[Boolean] should be(false)
    }
  }
  "nextPlayerToJson" should {
    "return a Json representation of the next player" in {
      val playerJson = (new UserController).nextPlayerToJson
      (playerJson \ "name").as[String] should be("White")
      (playerJson \ "value").as[Int] should be(2)
      (playerJson \ "isBot").as[Boolean] should be(true)
    }
  }
  "playersToJson" should {
    "return a JsObject representation of the players" in {
      val controller = new UserController
      val playersJson = Json.parse(controller.playersToJson.toString)
      val playersList = (playersJson \ "players").as[List[JsObject]]
      val playerOne = playersList.head
      val playerTwo = playersList(1)
      (playerOne \ "name").as[String] should be("Black")
      (playerOne \ "value").as[Int] should be(1)
      (playerOne \ "isBot").as[Boolean] should be(false)
      (playerTwo \ "name").as[String] should be("White")
      (playerTwo \ "value").as[Int] should be(2)
      (playerTwo \ "isBot").as[Boolean] should be(true)
    }
  }
}
