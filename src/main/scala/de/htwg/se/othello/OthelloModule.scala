package de.htwg.se.othello

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import de.htwg.se.othello.model.databaseComponent.GameDaoInterface
import de.htwg.se.othello.model.databaseComponent.daoSlickImpl.GameDao
import de.htwg.se.othello.model.fileIOComponent.{FileIOInterface, fileIoXmlImpl}
import net.codingwell.scalaguice.ScalaModule

class OthelloModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    install(new FactoryModuleBuilder()
      .implement(classOf[BoardInterface], classOf[Board])
      .build(classOf[BoardFactory]))
    bind[ControllerInterface].to[Controller]
    bind[FileIOInterface].to[fileIoXmlImpl.FileIO]
    bind[GameDaoInterface].to[GameDao]
  }
}
