package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}
class BotSpec extends WordSpec with Matchers {
  "A bot created with only value parameter" should {
    "have a default name" in {
      Bot(1).toString should be("Black")
    }
  }
}
