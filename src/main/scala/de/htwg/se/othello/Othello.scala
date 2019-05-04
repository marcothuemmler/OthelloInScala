package de.htwg.se.othello

import de.htwg.se.othello.aview.Tui
import de.htwg.se.othello.controller.Controller
import de.htwg.se.othello.model.{Bot, Player}
import scala.io.StdIn.readLine

object Othello {

  val players: Vector[Player] = Vector(new Player(1), new Bot(2))
  val controller = new Controller(players)
  val tui = new Tui(controller)
  controller.notifyObservers()

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
