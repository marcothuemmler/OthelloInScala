//package de.htwg.se.othello.model.fileIOComponent.fileIoXmlImpl
//
//import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
//
//import org.scalatest.{Matchers, WordSpec}
//
//class FileIOSpec extends WordSpec with Matchers{
//  "An XML File IO" should {
//    "save and restore the whole game" in {
//      val controller = new Controller
//      val fileIO = new FileIO
//      fileIO.save(controller.boardController.board, controller.getCurrentPlayer, controller.difficulty)
//      val res = fileIO.load
//      res.isSuccess should be(true)
//    }
//  }
//}
