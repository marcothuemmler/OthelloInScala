package de.htwg.se.othello

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.aview.{HttpServer, Tui}
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.io.StdIn.readLine

object Othello {

  val injector: Injector = Guice.createInjector(new OthelloModule)

  val globalController: ControllerInterface = injector.instance[ControllerInterface]
  val webServer = new HttpServer(globalController)
  val tui = new Tui(globalController)
  if (!sys.env.contains("DOCKER_ENV")) new SwingGui(globalController)

  globalController.newGame

  def main(args: Array[String]): Unit = {
    while (true) tui.processInputLine(readLine)
    webServer.unbind()
  }
}
