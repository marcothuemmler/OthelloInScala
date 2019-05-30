package de.htwg.se.othello

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.aview.gui._
import de.htwg.se.othello.controller.Controller

import scala.io.StdIn.readLine

object Othello {

  val controller = new Controller
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.newGame()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    while(true) {
      input = readLine()
      tui.processInputLine(input)
    }
  }
}
