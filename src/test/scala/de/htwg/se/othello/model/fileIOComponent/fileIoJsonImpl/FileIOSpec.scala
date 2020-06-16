package de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl

import scala.reflect.io.File

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileIOSpec extends AnyWordSpec with Matchers{
  "A Json File IO" should {
    "save and restore the whole game" in {
      val fileIO = new FileIO
      val board = new Board(8)
      val player = Player("Bob", 1)
      fileIO.save("savegame.json")(board, player, "Normal")
      val res = fileIO.load("savegame.json")
      File("savegame.json").delete
      res.isSuccess should be(true)
    }
  }
}
