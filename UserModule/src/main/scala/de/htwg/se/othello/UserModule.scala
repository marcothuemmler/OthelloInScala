package de.htwg.se.othello

import com.google.inject.AbstractModule
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController
import de.htwg.se.othello.model.databaseComponent.PlayerDaoInterface
import de.htwg.se.othello.model.databaseComponent.daoSlickImpl.PlayerDao
import net.codingwell.scalaguice.ScalaModule

class UserModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserControllerInterface].to[UserController]
    bind[PlayerDaoInterface].to[PlayerDao]
  }
}
