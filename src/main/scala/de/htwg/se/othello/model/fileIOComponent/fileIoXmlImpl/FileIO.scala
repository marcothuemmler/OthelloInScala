package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface{
  override def load: Option[BoardInterface] = {
    var boardOption: Option[BoardInterface] = None
    val file = scala.xml.XML.loadFile("grid.xml")
    val sizeAttr = file \\ "grid" \ "@size"
    val size = sizeAttr.text.toInt
    val injector = Guice.createInjector(new OthelloModule)

    /**
    size match {
      case 1 => boardOption = Some(injector.instance[BoardInterface](Names.named("tiny")))
      case 4 => boardOption = Some(injector.instance[BoardInterface](Names.named("small")))
      case 8 => boardOption = Some(injector.instance[BoardInterface](Names.named("normal")))
      case _ =>
    }
    **/
    val cellNodes = file \\ "cell"
    boardOption match {
      case Some(board) =>
        var _board = board
        for (cell <- cellNodes) {
          val row: Int = (cell \ "@row").text.toInt
          val col: Int = (cell \ "@col").text.toInt
          val value: Int = cell.text.trim.toInt
          _board = _board.setByOpp(row, col, value)
        }
        boardOption = Some(_board)
      case None =>
    }
    boardOption
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
    <board size={board.size.toString}>
      {for {
      row <- 0 until board.size
      col <- 0 until board.size
    } yield cellToXml(board, row, col)}
    </board>
  }

  def cellToXml(board: BoardInterface, row: Int, col: Int): Elem = {
    <cell row={row.toString} col={col.toString}>
      {board.valueOf(row, col)}
    </cell>
  }
}
