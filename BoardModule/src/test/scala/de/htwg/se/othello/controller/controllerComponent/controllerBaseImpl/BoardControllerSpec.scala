package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.JsValue

class BoardControllerSpec extends AnyWordSpec with Matchers {

  "size" should {
    "return the size of the board" in {
      val controller = new BoardController
      controller.size should equal(8)
    }
  }
  "resize" should {
    "create a new board that is two fields larger then the current one on input '+'" in {
      val controller: BoardController = new BoardController
      controller.resizeBoard("+")
      controller.size should equal(10)
    }
    "create a new board that is two fields smaller then the current one on input '-'" in {
      val controller = new BoardController
      controller.resizeBoard("-")
      controller.size should equal(6)
    }
    "do nothing on input '-' if the board is of size 4" in {
      val controller = new BoardController
      controller.createBoard(4)
      val sizeBefore = controller.size
      controller.resizeBoard("-")
      controller.size should equal(sizeBefore)
    }
    "create a new board of size 8 on input '.'" in {
      val controller = new BoardController
      controller.resizeBoard(".")
      controller.size should equal(8)
    }
  }
  "createBoard" should {
    "create a new board of the specified size" in {
      val controller = new BoardController
      controller.createBoard(16)
      controller.size should equal(16)
    }
  }
  "setBoard" should {
    "set the board to the one passed as parameter" in {
      val controller = new BoardController
      val board = controller.board
      controller.setBoard((new CreateBoardStrategy).createNewBoard(12))
      controller.board should not equal board
    }
  }
  "changeHighlight" should {
    "Highlight valid moves" in {
      val controller = new BoardController
      controller.changeHighlight(1)
      controller.valueOf(2,3) shouldBe(-1)
    }
  }
  "moves" should {
    "return a map of valid moves" in {
      val controller = new BoardController
      controller.moves(1) shouldBe Map((3,4) -> Vector((3,2), (5,4)), (4,3) -> Vector((2,3), (4,5)))
    }
  }
  "gameOver" should {
    "return false if there are valid moves to be made" in {
      val controller = new BoardController
      controller.gameOver shouldBe false
    }
    "return true if there are no valid moves to be made" in {
      val controller = new BoardController
      (0 to 7).foreach(o => controller.board = controller.board.flipLine((o, 0), (o, 7))(1))
      controller.gameOver shouldBe true
    }
  }
  "valueOf" should {
    "return the value of the specified square" in {
      val controller = new BoardController
      controller.valueOf(4,3) shouldBe 1
    }
  }
  "count" should {
    " count the amount of pieces of the specified value" in {
      val controller = new BoardController
      controller.count(1) shouldBe 2
    }
  }
  "boardToString" should {
    "return a String representation of the board" in {
      val controller = new BoardController
      controller.boardToString shouldBe a[String]
    }
  }
  "boardToHtml" should {
    "return a HTML representation of the board" in {
      val controller = new BoardController
      controller.boardToHtml shouldBe a[String]
    }
  }
  "toJson" should {
    "return a JsObject of the board" in {
      val controller = new BoardController
      val boardJson = controller.toJson
      (boardJson \ "size").as[Int] shouldBe 8
    }
  }
  "movesToJson" should {
    "return a JsObject containing the valid moves for the specified value" in {
      val controller = new BoardController
      val movesList = controller.movesToJson(1).as[List[JsValue]]
      movesList.map(e =>
        (e \ "key").as[(Int, Int)] -> (e \ "value").as[Seq[(Int, Int)]]).toMap should equal(controller.moves(1))
    }
  }
}
