package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.Controller
import de.htwg.se.othello.model.Bot
import de.htwg.se.othello.util.Observer

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "n" => controller.newGame()
      case "h" => controller.highlight()
      case "s" => println(controller.suggestions)
      case _ =>
        if (controller.set(input)) {
          if (controller.player.isInstanceOf[Bot]) {
            Thread.sleep(500)
            controller.botSet()
          }
        } else {
          println(f"Please try again. ${controller.suggestions}")
        }
    }
  }

  override def update(): Unit = println(controller.boardToString)
}
