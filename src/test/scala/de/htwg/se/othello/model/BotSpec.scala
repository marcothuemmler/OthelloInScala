package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}
class BotSpec extends WordSpec with Matchers {
  "A bot created with only 2 parameters" should {
    "have a default name" in {
      val newBot = new Bot(2, Game(new Board))
      newBot.toString should be("Bot2")
    }
  }
  "getMove " should {
    "return a tuple " in {
      val bot = new Bot("", 1 , Game(new Board))
      bot.getMove shouldBe a[(_, _)]
      bot.getMove._1 should be < 8
      bot.getMove._1 should be > -1
      bot.getMove._2 should be < 8
      bot.getMove._2 should be > -1
    }
  }
}
