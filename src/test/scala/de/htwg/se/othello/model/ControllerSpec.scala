package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  val mvc = new Controller
  var board = new Board
  val player1 = new Player(1, board)
  val players: Vector[Player] = Vector(player1, new Player(2, board))
  "Suggestions" should {
    "return possible moves correctly mapped to board layout" in {
      mvc.suggestions(player1) should be(List("C4", "D3", "E6", "F5"))
    }
  }
  "mapOut" should {
    "take an Integer and give us back a String" in {
      mvc.mapOutput(0, 0) should be("A1")
    }
  }
  "Winner" should {
    "declare a draw if the amount of tiles are equal" in {
      mvc.result(players) should be("Draw. 2:2")
    }
    "declare a winner in the amount of tiles are not equal" in {
      player1.set(2, 3)
      mvc.result(players) should be("Player1 wins by 4:1!")
    }
  }
  "mapInput" should {
    "take a Char and give back the correct matching Integer" in {
      mvc.mapToBoard("A1") should be(0, 0)
      mvc.mapToBoard("H8") should be(7, 7)
    }
    "return an invalid value if the input does not match" in {
      mvc.mapToBoard("z9") should be(25,8)
    }
  }
  "end" should {
    "be false if a least one player still has moves left" in {
      mvc.gameOver(players) should be (false)
    }
  }
}
