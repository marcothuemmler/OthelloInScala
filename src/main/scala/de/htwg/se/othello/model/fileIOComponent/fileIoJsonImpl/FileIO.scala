package de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl

import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load: Option[BoardInterface] = {
    var boardOption :Option[BoardInterface] = None
    val source = Source.fromFile("board.json")
    val content: String = source.getLines.mkString
    val json: JsValue = Json.parse(content)
    source.close()
    val size = (json \ "board" \ "size").get.toString.toInt
    val injector = Guice.createInjector(new OthelloModule)
    size match {
      case 1 => boardOption = Some(injector.instance[BoardInterface](Names.named("tiny")))
      case 4 => boardOption = Some(injector.instance[BoardInterface](Names.named("small")))
      case 8 => boardOption = Some(injector.instance[BoardInterface](Names.named("normal")))
      case _ =>
    }
    var board: BoardInterface = new Board(size)
    for (index <- 0 until size * size) {
      val col = (json \\ "col") (index).as[Int]
      val row = (json \\ "row") (index).as[Int]
      val value = (json \\ "value") (index).as[Int]
      board = board.flip(col, row, value)
    }
    board
  }

  override def save(board: BoardInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("board.json"))
    pw.write(Json.prettyPrint(boardToJson(board)))
    pw.close()
  }

  def boardToJson(board: BoardInterface): JsObject = {
    Json.obj(
      "board" -> Json.obj(
        "size" -> JsNumber(board.size),
        "content" -> Json.toJson(
          for {
            col <- 0 until board.size
            row <- 0 until board.size
          } yield Json.obj(
            "value" -> board.valueOf(col, row),
            "col" -> col,
            "row" -> row
          )
        )
      )
    )
  }
}
