package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface

import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
  override def load: (BoardInterface, Player, Int) = {
    val file = scala.xml.XML.loadFile("savegame.xml")
    val sizeAttr = file \\ "board" \ "@size"
    val size = sizeAttr.text.toInt
    var board = new Board(size)
    val cellNodes = file \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val value: Int = cell.text.trim.toInt
      board = board.flip(row, col, value)
    }
    val playerName = (file \\ "player" \ "@name").text.toString
    val playerValue = (file \\ "player" \ "@value").text.toInt
    val player = Player(playerName, playerValue)
    val diff = (file \\ "diff" \ "@level").text.toInt
    (board, player, diff)
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
      <board size={board.size.toString}>
        {for {
        row <- 0 until board.size
        col <- 0 until board.size
      } yield cellToXml(board, row, col)}
      </board>
      <player name={player.name.toString} value={player.value.toString}>
      </player>
      <diff level={difficulty.toString}></diff>
    </root>
  }

  def cellToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <cell row={row.toString} col={col.toString}>
      {board.valueOf(row, col)}
    </cell>
  }
}
