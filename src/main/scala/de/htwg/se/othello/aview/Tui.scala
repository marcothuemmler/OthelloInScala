package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.Controller
import de.htwg.se.othello.util.Observer

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "n" => controller.newGame()
      case "h" => controller.highlight()
      case "s" => println(controller.suggestions)
      case "0" | "1" | "2" => controller.setupPlayers(input)
      case _ =>
        input.toList.map(in => in.toString) match {
          case col :: row :: Nil =>
            val square = controller.mapToBoard(col + row)
            controller.setAndNext(square)
          case _ =>
            println("Please try again. " + controller.suggestions)
        }
    }
  }

  override def update(): Unit = println(controller.status)
}
