package de.htwg.se.othello

import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.BoardController

object BoardModule {

  def main(args: Array[String]): Unit = {
    new BoardModuleHttpServer(new BoardController)
  }
}
