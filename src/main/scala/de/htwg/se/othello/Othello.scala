package de.htwg.se.othello

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller

import scala.io.StdIn.readLine

object Othello {

  def main(args: Array[String]): Unit = {
    val controller = new Controller
    var input: String = ""
    val tui = new Tui(controller)
    if (args.length == 0) {
      new SwingGui(controller)
    }
    controller.newGame
    if (args.nonEmpty) {
      input = args(0)
      tui.processInputLine(input)
    } else {
      while (true) {
        input = readLine
        tui.processInputLine(input)
      }
    }
  }
}
