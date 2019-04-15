package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class MVCRunSpec extends WordSpec with Matchers {
  val mvc = new MVCRun
  var game = new Game
  val players: Vector[Player] = Vector(new Player(1, game), new Player(2, game))
  "Suggestions" should {
    "return possible moves correctly mapped to board layout" in {
      mvc.suggestions(players(0)) should be(List("C4", "D3", "E6", "F5"))
    }
  }
  "mapOut" should {
    "take an Integer and give us back a String" in {
      mvc.mapOutput(0) should be("A")
    }
  }
  "Winner" should {
    "declare a draw if the amount of tiles are equal" in {
      mvc.winner(players) should be("Draw. 2:2")
    }
    "declare a winner in the amount of tiles are not equal" in {
      players(0).set(2, 3)
      mvc.winner(players) should be("Player1 wins by 4:1!")
      game = new Game
    }
  }
  "mapInput" should {
    "take a Char and give back the correct matching Integer" in {
      mvc.mapInput('A') should be(0)
      mvc.mapInput('a') should be(0)
      mvc.mapInput('B') should be(1)
      mvc.mapInput('b') should be(1)
      mvc.mapInput('c') should be(2)
      mvc.mapInput('C') should be(2)
      mvc.mapInput('d') should be(3)
      mvc.mapInput('D') should be(3)
      mvc.mapInput('e') should be(4)
      mvc.mapInput('E') should be(4)
      mvc.mapInput('f') should be(5)
      mvc.mapInput('F') should be(5)
      mvc.mapInput('g') should be(6)
      mvc.mapInput('G') should be(6)
      mvc.mapInput('h') should be(7)
      mvc.mapInput('H') should be(7)
    }
    "return an invalid value if the input does not match" in {
      mvc.mapInput('z') should be(42)
      mvc.mapInput(0) should be (42)
    }
  }
}
