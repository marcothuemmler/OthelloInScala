package de.htwg.se.othello

import com.google.inject.AbstractModule
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.fileIOComponent.FileIOInterface
import de.htwg.se.othello.model.fileIOComponent.fileIoJsonImpl
import net.codingwell.scalaguice.ScalaModule

class OthelloModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
    bind[BoardInterface].to[Board]
    bind[FileIOInterface].to[fileIoJsonImpl.FileIO]
  }
}
