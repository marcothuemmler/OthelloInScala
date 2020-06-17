package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.BoardModule
import de.htwg.se.othello.controller.controllerComponent.BoardControllerInterface
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import de.htwg.se.othello.model.database.Dao
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.json.{JsObject, JsValue, Json}

class BoardController extends BoardControllerInterface {

  var board: BoardInterface = (new CreateBoardStrategy).createNewBoard(8)
  val injector: Injector = Guice.createInjector(new BoardModule)
  val dao: Dao = injector.instance[Dao]

  def size: Int = board.size

  def resizeBoard: String =>  Unit = {
    case "+" => createBoard(size + 2)
    case "-" => if (size > 4) createBoard(size - 2)
    case "." => if (size != 8) createBoard(8)
  }

  def createBoard(size: Int): Unit = {
    board = (new CreateBoardStrategy).createNewBoard(size)
  }

  def setBoard(board: BoardInterface): Unit = this.board = board

  def changeHighlight(implicit value: Int): Unit = board = board.changeHighlight

  def moves(implicit value: Int): Map[(Int, Int), Seq[(Int, Int)]] = board.moves

  def gameOver: Boolean = board.gameOver

  def valueOf(col: Int, row: Int): Int = board.valueOf(col, row)

  def count(value: Int): Int = board.count(value)

  def boardToString: String = board.toString

  def boardToHtml: String = board.toHtml

  def toJson: JsObject = board.toJson

  def movesToJson(implicit value: Int): JsValue = {
    Json.toJson(moves.map(e => Json.obj("key" -> e._1, "value" -> e._2)))
  }
}
