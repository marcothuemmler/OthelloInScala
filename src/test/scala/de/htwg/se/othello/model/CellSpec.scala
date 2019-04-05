package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
  "A Cell when not set" should {
    val emptyCell = Cell(0)
    "have value 0" in {
      emptyCell.value should be(0)
    }
  }
}
