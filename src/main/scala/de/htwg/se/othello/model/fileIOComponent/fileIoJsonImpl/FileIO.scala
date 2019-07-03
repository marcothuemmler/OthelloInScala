package de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import play.api.libs.json._

import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface {

  override def load = Try {
    val source = Source.fromFile("savegame.json")
    val json: JsValue = Json.parse (source.getLines.mkString)
    source.close ()
    val size = (json \ "board" \ "size").as[Int]
    var board: BoardInterface = new Board (size)
    for {
      index <- 0 until size * size
      row = (json \\ "row") (index).as[Int]
      col = (json \\ "col") (index).as[Int]
      value = (json \\ "value") (index).as[Int]
    } board = board.flip (row, col, value)
    val color = (json \ "player" \ "value").as[Int]
    val name = (json \ "player" \ "name").toString
    val difficulty = (json \ "difficulty").as[Int]
    (board, Player(name, color), difficulty)
  }

  override def save(board: BoardInterface, player: Player, difficulty: Int): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("savegame.json"))
    pw.write(Json.prettyPrint(stateToJson(board, player, difficulty)))
    pw.close()
  }

  def stateToJson(board: BoardInterface, player: Player, difficulty: Int): JsObject = {
    Json.obj(
      "board" -> Json.obj(
        "size" -> board.size,
        "squares" -> Json.toJson(
          for {
            row <- 0 until board.size
            col <- 0 until board.size
          } yield Json.obj(
            "value" -> board.valueOf(row, col),
            "row" -> row,
            "col" -> col
          )
        )
      ),
      "player" -> Json.obj(
        "name" -> player.name,
        "value" -> player.value,
      ),
      "difficulty" -> difficulty
    )
  }
}