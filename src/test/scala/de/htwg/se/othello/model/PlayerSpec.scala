package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  val player = Player("Otto", 1)

  "A player" should {
    "have a toString method" in {
      player.toString should be("Otto")
    }
  }
  "A Player without name parameter" should {
    "have a default name" in {
      new Player(2).toString should be("White")
    }
  }
  "isBot" should {
    "return false if the player is a human player" in {
      player.isBot should be(false)
    }
    "return true if the player is a bot" in {
      new Bot(2).isBot should be(true)
    }
  }
}
