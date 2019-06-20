package de.htwg.se.othello

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.aview.gui.SwingGui
import de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl.Controller

import scala.io.StdIn.readLine

object Othello {

  val controller = new Controller
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.newGame

  def main(args: Array[String]): Unit = {
    while (true) {
      tui.processInputLine(readLine)
    }
  }
}
