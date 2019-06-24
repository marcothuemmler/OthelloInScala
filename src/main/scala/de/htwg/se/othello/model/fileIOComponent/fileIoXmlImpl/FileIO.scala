package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface

class FileIO extends FileIOInterface{
  override def load: BoardInterface = new Board

  override def save(grid: BoardInterface): Unit = ()
}
