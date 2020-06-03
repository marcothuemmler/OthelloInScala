package de.htwg.se.othello

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.{BoardController, Controller, UserController}
import de.htwg.se.othello.controller.controllerComponent.{BoardControllerInterface, ControllerInterface, UserControllerInterface}
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import de.htwg.se.othello.model.fileIOComponent.{FileIOInterface, fileIoXmlImpl}
import net.codingwell.scalaguice.ScalaModule

class OthelloModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    install(new FactoryModuleBuilder()
      .implement(classOf[BoardInterface], classOf[Board])
      .build(classOf[BoardFactory]))
    bind[ControllerInterface].to[Controller]
    bind[UserControllerInterface].to[UserController]
    bind[BoardControllerInterface].to[BoardController]
    bind[FileIOInterface].to[fileIoXmlImpl.FileIO]
  }
}
