package de.htwg.se.othello

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.aview.{HttpServer, Tui}
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.io.StdIn.readLine

object Othello {

  val injector: Injector = Guice.createInjector(new OthelloModule)
  val controller: ControllerInterface = injector.instance[ControllerInterface]
  val webserver = new HttpServer(controller)
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.newGame

  def main(args: Array[String]): Unit = {
    while (true) tui.processInputLine(readLine)
    webserver.unbind()
  }
}
