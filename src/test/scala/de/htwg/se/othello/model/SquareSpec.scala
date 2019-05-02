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
      square_1.toString should be("○|")
      square2.toString should be("●|")
      square1.toString should be(f"\u001B[34m●\u001B[0m|")
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
  "isHighlighted" should {
    "be true if the square is highlighted" in {
      square_1.isHighlighted should be (true)
    }
    "be false if the square i snot highlighted" in {
      square0.isHighlighted should be (false)
    }
  }
}
