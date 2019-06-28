package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
  override def load: BoardInterface = {
    val file = scala.xml.XML.loadFile("board.xml")
    val size = (file \\ "board" \ "@size").text.toInt

    var board = new Board(size)

    val squares = file \\ "square"
    for {
      square <- squares
      row = (square \ "@row").text.toInt
      col = (square \ "@col").text.toInt
      value = square.text.trim.toInt
    } board = board.flip(row, col, value)
    board
  }

  def save(board: BoardInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("board.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(boardToXml(board))
    pw.write(xml)
    pw.close()
  }

  def boardToXml(board: BoardInterface): Elem = {
    <board size={board.size.toString}>
      {for {
      row <- 0 until board.size
      col <- 0 until board.size
    } yield squareToXml(board, row, col)}
    </board>
  }

  def squareToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <square row={row.toString} col={col.toString}>
      {board.valueOf(row, col)}
    </square>
  }
}
