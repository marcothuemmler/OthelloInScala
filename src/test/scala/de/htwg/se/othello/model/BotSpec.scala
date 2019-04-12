package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}
class BotSpec extends WordSpec with Matchers {
  "getMove " should {
    "return a tupel " in{
      val bot = new Bot("", 1 , Game(new Board))

      bot.getMove(bot.moves) shouldBe a[(_, _)]
      bot.getMove(bot.moves)._1 should be < 8
      bot.getMove(bot.moves)._1 should be > -1
      bot.getMove(bot.moves)._2 should be < 8
      bot.getMove(bot.moves)._2 should be > -1

    }
  }
}
