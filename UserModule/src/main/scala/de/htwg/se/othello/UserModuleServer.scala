package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.UserController

import scala.concurrent.Future

class UserRestServer(implicit val system: ActorSystem, implicit val controller: UserControllerInterface) extends UserModuleHttpService {
  def startServer(address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object UserModuleServer {

  def main(args: Array[String]): Unit = {

    implicit val actorSystem: ActorSystem = ActorSystem("user-server")
    implicit val controller: UserControllerInterface = new UserController

    val server = new UserRestServer()
    server.startServer("localhost", 8082)
  }
}
