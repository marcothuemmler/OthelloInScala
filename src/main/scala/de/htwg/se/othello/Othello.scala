package de.htwg.se.othello

import com.google.inject.{Guice, Injector}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface

import scala.io.StdIn.readLine

object Othello {

  val injector: Injector = Guice.createInjector(new OthelloModule)
  val controller: ControllerInterface = injector.instance[ControllerInterface]
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.newGame

  def main(args: Array[String]): Unit = {
    var input: String = ""
    if (args.length>0) input=args(0)
    if (!input.isEmpty) tui.processInputLine(input)
    else do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q")
  }
}
