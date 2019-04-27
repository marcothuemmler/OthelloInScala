package de.htwg.se.othello.model

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
      p.toString should be("Player2")
    }
  }
}
