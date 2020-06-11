package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.reflect.io.File

class FileIOSpec extends AnyWordSpec with Matchers{
  "An XML File IO" should {
    "save and restore the whole game" in {
      val fileIO = new FileIO
      val board = new Board(8)
      val player = Player("Bob", 1)
      fileIO.save("savegame.xml")(board, player, "Normal")
      val res = fileIO.load("savegame.xml")
      File("savegame.xml").delete
      res.isSuccess should be(true)
    }
  }
}
