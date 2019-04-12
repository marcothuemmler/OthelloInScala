package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell when not set" should {
    val emptyCell = Cell(0)
    "have value 0" in {
      emptyCell.value should be(0)
    }
    "have a toString representation" in {
      emptyCell.toString should be("_|")
    }
  }
  "A cell when set" should {
    "have a value" in {
      val setCell = Cell(1)
      val cell2 = Cell(2)
      setCell.value should be(1)
      cell2.value should be(2)
    }
  }
  "A cell" should {
    "have a nice toString representation" in {
      val printCell = Cell(-1)
      val cell2 = Cell(2)
      val cell1 = Cell(1)
      printCell.toString should be("x|")
      cell2.toString should be("2|")
      cell1.toString should be(f"\u001B[34m${cell1.value}%d\u001B[0m|")
    }
  }
}
