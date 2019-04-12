package de.htwg.se.othello.model

import org.scalatest.{Matchers, WordSpec}

class GameSpec extends WordSpec with Matchers {
    val game = Game(new Board)

    "flip" should {
      "set the value of the cell to new value" in {
        game.flip(7, 7, 2)
        game.board.field(7)(7).value should be(2)
      }
      "set the value of the cell vertically" in {

        game.flipline((5,4), (3,4), 1 )
        game.board.field(4)(4).value should be (1)
      }
      "set the value of the cell horizontally " in{

        game.flipline((4,3),(4,5),1)
        game.board.field(4)(4).value should be (1)
      }
      "set the value from up-left to down-right" in {

        game.flipline((2,2), (5,5),1)
        game.board.field(3)(3).value should be (1)
        game.board.field(4)(4).value should be (1)
      }
      "set the value from up-right to down-left" in{

        game.flipline((2,5),(5,2),2)
        game.board.field(4)(3).value should be (2)
        game.board.field(3)(4).value should be (2)
      }








    }
}
