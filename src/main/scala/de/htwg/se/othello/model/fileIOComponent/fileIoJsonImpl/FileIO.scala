package de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl

import boardComponent.boardBaseImpl.CreateBoardStrategy
import boardComponent.{BoardInterface, Player}
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface {

  //val injector: Injector = Guice.createInjector(new OthelloModule)

  def load: Try[(BoardInterface, Player, String)] = Try {
    val source = Source.fromFile("savegame.json")
    val json: JsValue = Json.parse (source.getLines.mkString)
    source.close ()
    val size = (json \ "board" \ "size").as[Int]
    //var board: BoardInterface = injector.instance[BoardFactory].create(size)
    var board = (new CreateBoardStrategy).createNewBoard(size)
    for {
      index <- 0 until size * size
      row = (json \\ "row") (index).as[Int]
      col = (json \\ "col") (index).as[Int]
      value = (json \\ "value") (index).as[Int]
    } board = board.flipLine((row, col), (row, col), value)
    val name = (json \ "player" \ "name").as[String]
    val color = (json \ "player" \ "value").as[Int]
    val difficulty = (json \ "difficulty").as[String]
    (board, Player(name, color), difficulty)
  }

  override def save(board: BoardInterface, player: Player, difficulty: String): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("savegame.json"))
    pw.write(Json.prettyPrint(stateToJson(board, player, difficulty)))
    pw.close()
  }

  def stateToJson(board: BoardInterface, player: Player, difficulty: String): JsObject = {
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
