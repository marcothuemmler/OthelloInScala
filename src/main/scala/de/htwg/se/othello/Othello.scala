package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.aview.{OthelloHttpService, Tui}
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.concurrent.Future
import scala.io.StdIn.readLine

class OthelloServer(implicit val system: ActorSystem, implicit val controller: ControllerInterface) extends OthelloHttpService {
  def startServer(address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object Othello {

  def main(args: Array[String]): Unit = {

    val injector: Injector = Guice.createInjector(new OthelloModule)
    implicit val globalController: ControllerInterface = injector.instance[ControllerInterface]
    implicit val actorSystem: ActorSystem = ActorSystem("othello-server")
    val webServer = new OthelloServer
    val tui = new Tui(globalController)
    if (!sys.env.contains("DOCKER_ENV")) new SwingGui(globalController)

    webServer.startServer("0.0.0.0", 8080)
    globalController.newGame

    while (true) {
      tui.processInputLine(readLine)
    }
  }
}
