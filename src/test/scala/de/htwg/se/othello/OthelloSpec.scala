package de.htwg.se.othello

import org.scalatest.{Matchers, WordSpec}

class OthelloSpec extends WordSpec with Matchers {
  "The Othello main class" should {
    "accept text input as argument without readline loop, to test it from command line " in {
      Othello.main(Array[String]("s"))
    }
  }
}
