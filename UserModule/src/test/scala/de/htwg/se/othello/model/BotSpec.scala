package de.htwg.se.othello.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BotSpec extends AnyWordSpec with Matchers {
  "A bot created with only value parameter" should {
    "have a default name" in {
      new Bot(1).toString should be("Black")
    }
  }
}
