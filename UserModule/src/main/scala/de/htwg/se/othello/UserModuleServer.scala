package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.google.inject.Guice
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.othello.model.database.slick.Slick

import scala.concurrent.Future

class UserModuleServer(implicit val system: ActorSystem, implicit val controller: UserControllerInterface) extends UserModuleHttpService {
  def startServer(address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object UserModuleServer {

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(new UserModule)
    implicit val controller: UserControllerInterface = injector.instance[UserControllerInterface]

    implicit val actorSystem: ActorSystem = ActorSystem("user-server")
    Slick()
    val server = new UserModuleServer()
    server.startServer("0.0.0.0", 8082)
  }
}
