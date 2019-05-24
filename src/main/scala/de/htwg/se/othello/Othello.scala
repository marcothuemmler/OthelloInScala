package de.htwg.se.othello

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.controller.Controller

import scala.io.StdIn.readLine

object Othello {

  val controller = new Controller
  val tui = new Tui(controller)
  controller.newGame()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    if (args.length > 0) input = args(0)
    if (!input.isEmpty) tui.processInputLine(input)
    else do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q")
  }
}
