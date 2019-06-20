package de.htwg.se.othello.model

import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  val board = new Board
  val player = Player("Otto", 1)
  val p = new Player(2)

  "A player" should {
    "have a toString method" in {
      player.toString should be("Otto")
    }
  }
  "A Player without name parameter" should {
    "have a default name" in {
      p.toString should be("White")
    }
  }
  "isBot" should {
    "return false if the player is a human player" in {
      val player = new Player(1)
      player.isBot should be(false)
    }
    "return true if the player is a bot" in {
      val bot = new Bot(2)
      bot.isBot should be(true)
    }
  }
}
