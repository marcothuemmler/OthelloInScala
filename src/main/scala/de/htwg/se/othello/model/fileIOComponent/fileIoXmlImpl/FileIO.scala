package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import boardComponent.boardBaseImpl.CreateBoardStrategy
import boardComponent.{BoardInterface, Player}
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface

import scala.util.Try
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {

  //val injector: Injector = Guice.createInjector(new OthelloModule)

  def load: Try[(BoardInterface, Player, String)] = Try {
    val file = scala.xml.XML.loadFile("savegame.xml")
    val size = (file \\ "board" \ "@size").text.toInt
    //var board = injector.instance[BoardFactory].create(size)
    var board = (new CreateBoardStrategy).createNewBoard(size)
    val squares = file \\ "square"
    for {
      square <- squares
      (row, col) = ((square \ "@row").text.toInt ,(square \ "@col").text.toInt)
      value = square.text.trim.toInt
    } board = board.flipLine((row, col), (row, col), value)
    val name = (file \\ "player" \ "@name").text.toString
    val color = (file \\ "player").text.trim.toInt
    val difficulty = (file \\ "difficulty").text.trim
    (board, Player(name, color), difficulty)
  }

  def save(board: BoardInterface, player: Player, difficulty: String): Unit = {
    saveString(board, player, difficulty)
  }

  def saveString(board: BoardInterface, player: Player, difficulty: String): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("savegame.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val stateXml = prettyPrinter.format(stateToXml(board, player, difficulty))
    pw.write(stateXml)
    pw.close()
  }

  def stateToXml(board: BoardInterface, player: Player, difficulty: String): Elem = {
    <savegame>
      <board size={ board.size.toString }>
        { for {
        row <- 0 until board.size
        col <- 0 until board.size
      } yield squareToXml(board, row, col) }
      </board>
      <player name={ player.name }> { player.value } </player>
      <difficulty> { difficulty } </difficulty>
    </savegame>
  }

  def squareToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <square row={ row.toString } col={ col.toString }>
      { board.valueOf(row, col) }
    </square>
  }
}
