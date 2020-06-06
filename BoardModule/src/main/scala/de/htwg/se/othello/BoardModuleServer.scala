package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import de.htwg.se.othello.controller.controllerComponent.BoardControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.BoardController

import scala.concurrent.Future

class BoardRestServer(implicit val system: ActorSystem, implicit val controller: BoardControllerInterface) extends BoardModuleHttpService {
  def startServer(address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object BoardModuleServer {

  def main(args: Array[String]): Unit = {

    implicit val actorSystem: ActorSystem = ActorSystem("board-server")
    implicit val controller: BoardController = new BoardController

    val server = new BoardRestServer()
    server.startServer("localhost", 8081)
  }
}
