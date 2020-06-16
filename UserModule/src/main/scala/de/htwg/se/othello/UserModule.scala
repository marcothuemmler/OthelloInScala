package de.htwg.se.othello

import com.google.inject.AbstractModule
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController
import de.htwg.se.othello.model.database.Dao
import de.htwg.se.othello.model.database.slick.Slick
import net.codingwell.scalaguice.ScalaModule

class UserModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserControllerInterface].to[UserController]
    bind[Dao].to[Slick]
  }
}
