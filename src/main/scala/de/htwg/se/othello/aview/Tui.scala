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
      case "s" => println(s"Valid moves: ${controller.suggestions}")
      case _ =>
        input.length match {
          case 2 =>
            controller.set(controller.mapToBoard(input))
            if (controller.player.isInstanceOf[Bot] && !controller.gameOver) {
              Thread.sleep(500)
              controller.botSet()
            }
          case _ =>
            println(s"Please try again. Valid moves: ${controller.suggestions}")
        }
    }
  }

  override def update(): Unit = println(controller.boardToString)
}
