package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}
class BotSpec extends WordSpec with Matchers {
  val bot = new Bot(1)
  "A bot created with only value parameter" should {
    "have a default name" in {
      bot.toString should be("Black")
    }
  }
}
