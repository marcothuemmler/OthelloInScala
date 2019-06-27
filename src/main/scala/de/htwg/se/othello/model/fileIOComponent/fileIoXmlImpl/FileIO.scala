package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {
  override def load: BoardInterface = {
    var board: BoardInterface = null
    val file = scala.xml.XML.loadFile("grid.xml")
    val sizeAttr = file \\ "board" \ "@size"
    val size = sizeAttr.text.toInt
    board = new Board(size)
    /**
     * size match {
     * case 1 => board = Some(injector.instance[BoardInterface](Names.named("tiny")))
     * case 4 => board = Some(injector.instance[BoardInterface](Names.named("small")))
     * case 8 => board = Some(injector.instance[BoardInterface](Names.named("normal")))
     * case _ =>
     * }
     */
    val cellNodes = file \\ "cell"
        for (cell <- cellNodes) {
          val row: Int = (cell \ "@row").text.toInt
          val col: Int = (cell \ "@col").text.toInt
          val value: Int = cell.text.trim.toInt
          board = board.flip(row, col, value)
        }

    board
    }

  def save(board: BoardInterface): Unit = saveString(board)

  def saveString(grid: BoardInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("grid.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(boardToXml(grid))
    pw.write(xml)
    pw.close()
  }

  def boardToXml(board: BoardInterface): Elem = {
    <board size={ board.size.toString }>
      {
      for {
        row <- 0 until board.size
        col <- 0 until board.size
      } yield cellToXml(board, row, col) }
    </board>
  }

  def cellToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <cell row={ row.toString } col={ col.toString }>
      { board.valueOf(row, col) }
    </cell>
  }
}
