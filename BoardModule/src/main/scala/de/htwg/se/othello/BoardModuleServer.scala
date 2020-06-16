package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.google.inject.Guice
import de.htwg.se.othello.controller.controllerComponent.BoardControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.othello.model.database.slick.Slick

import scala.concurrent.Future

class BoardModuleServer(implicit val system: ActorSystem, implicit val controller: BoardControllerInterface) extends BoardModuleHttpService {
  def startServer(address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object BoardModuleServer {

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(new BoardModule)
    implicit val actorSystem: ActorSystem = ActorSystem("board-server")
    implicit val controller: BoardControllerInterface = injector.instance[BoardControllerInterface]

    Slick()

    val server = new BoardModuleServer()
    server.startServer("0.0.0.0", 8081)
  }
}
