package de.htwg.se.othello

import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController

object UserModule {

  def main(args: Array[String]): Unit = {
    new UserModuleHttpServer(new UserController)
  }
}
