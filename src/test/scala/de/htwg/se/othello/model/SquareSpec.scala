package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class SquareSpec extends WordSpec with Matchers {
  val square0 = Square(0)
  val square1 = Square(1)
  val square2 = Square(2)
  val square_1 = Square(-1)
  "A Square" should {
    "have a value" in {
      square0.value should be(0)
    }
    "have a nice toString representation" in {
      square0.toString should be("_|")
      square_1.toString should be("x|")
      square2.toString should be("2|")
      square1.toString should be(f"\u001B[34m${Square(1).value}%d\u001B[0m|")
    }
  }
  "isSet" should {
    "be true if the square contains a disk" in {
      square1.isSet should be(true)
    }
    "be false if the is no disk" in {
      square0.isSet should be (false)
    }
  }
}
