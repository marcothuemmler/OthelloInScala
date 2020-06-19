package de.htwg.se.othello

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import de.htwg.se.othello.controller.controllerComponent.BoardControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.BoardController
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import de.htwg.se.othello.model.databaseComponent.BoardDaoInterface
import de.htwg.se.othello.model.databaseComponent.daoMongoImpl.BoardDao
import net.codingwell.scalaguice.ScalaModule

class BoardModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    install(new FactoryModuleBuilder()
      .implement(classOf[BoardInterface], classOf[Board])
      .build(classOf[BoardFactory]))
    bind[BoardControllerInterface].to[BoardController]
    bind[BoardDaoInterface].to[BoardDao]
  }
}
