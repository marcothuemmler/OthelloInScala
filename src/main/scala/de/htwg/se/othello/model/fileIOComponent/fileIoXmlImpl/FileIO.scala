package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface

import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
  override def load: (BoardInterface, Player, Int) = {
    val file = scala.xml.XML.loadFile("savegame.xml")
    val size = (file \\ "board" \ "@size").text.toInt
    var board = new Board(size)
    val cellNodes = file \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val value: Int = cell.text.trim.toInt
      board = board.flip(row, col, value)
    }
    val name = (file \\ "player" \ "@name").text.toString
    val color = (file \\ "player").text.trim.toInt
    val difficulty = (file \\ "difficulty").text.trim.toInt
    (board, Player(name, color), difficulty)
  }

  def save(board: BoardInterface, player: Player, difficulty: Int): Unit = {
    saveString(board, player, difficulty)
  }

  def saveString(board: BoardInterface, player: Player, difficulty: Int): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("savegame.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val stateXml = prettyPrinter.format(stateToXml(board, player, difficulty))
    pw.write(stateXml)
    pw.close()
  }

  def stateToXml(board: BoardInterface, player: Player, difficulty: Int): Elem = {
    <root>
      <board size={ board.size.toString }>
        { for {
        row <- 0 until board.size
        col <- 0 until board.size
      } yield cellToXml(board, row, col) }
      </board>
      <player name={ player.name }> { player.value } </player>
      <difficulty> { difficulty } </difficulty>
    </root>
  }

  def cellToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <cell row={ row.toString } col={ col.toString }>
      { board.valueOf(row, col) }
    </cell>
  }
}
