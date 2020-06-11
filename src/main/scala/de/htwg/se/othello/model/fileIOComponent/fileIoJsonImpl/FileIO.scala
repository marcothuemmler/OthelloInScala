package de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface {

  val injector: Injector = Guice.createInjector(new OthelloModule)

  def load(dir: String): Try[(BoardInterface, Player, String)] = Try {
    val source = Source.fromFile(dir)
    val json: JsValue = Json.parse(source.getLines.mkString)
    val jsonBoard = (json \ "board").as[JsValue]
    val board = (new CreateBoardStrategy).fill(jsonBoard)
    val name = (json \ "player" \ "name").as[String]
    val color = (json \ "player" \ "value").as[Int]
    val difficulty = (json \ "difficulty").as[String]
    source.close()
    (board, Player(name, color), difficulty)
  }

  override def save(dir: String)(implicit board: BoardInterface, player: Player, difficulty: String): Unit = {
    import java.io._
    val pw = new PrintWriter(new File(dir))
    pw.write(Json.prettyPrint(stateToJson(board, player, difficulty)))
    pw.close()
  }

  def stateToJson(board: BoardInterface, player: Player, difficulty: String): JsObject = {
    Json.obj(
      "board" -> board.toJson,
      "player" -> player.toJson,
      "difficulty" -> difficulty
    )
  }
}
