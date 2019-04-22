package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}
class BotSpec extends WordSpec with Matchers {

  val bot = new Bot(1, new Board)

  "A bot created with only 2 parameters" should {
    "have a default name" in {
      bot.toString should be("Bot1")
    }
  }
  "getMove " should {
    "return a tuple with values between 0 and 7" in {
      val botMove = bot.getMove
      botMove shouldBe a[(_, _)]
      botMove._1 should be < 8
      botMove._1 should be > -1
      botMove._2 should be < 8
      botMove._2 should be > -1
    }
  }
}
