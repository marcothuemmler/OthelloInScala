package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.{Controller, GameStatus}
import de.htwg.se.othello.util.Observer

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "n" => controller.newGame()
      case "h" => controller.highlight()
      case "z" => controller.undo()
      case "y" => controller.redo()
      case "s" => println(controller.suggestions)
      case _ => input.toList.map(in => in.toString) match {
        case col :: row :: Nil =>
          val square = controller.mapToBoard(col + row)
          controller.set(square)
        case count :: Nil if 0 to 2 contains count.toInt =>
          controller.setupPlayers(count)
        case _ => println("Please try again. " + controller.suggestions)
      }
    }
  }

  override def update: Boolean = {
    if (controller.gameStatus != GameStatus.GAME_OVER) {
      println(GameStatus.message(controller.gameStatus))
      println(controller.boardToString)
    } else {
      println(controller.boardToString)
      println(GameStatus.message(controller.gameStatus))
    }
    controller.gameStatus = GameStatus.IDLE
    true
  }
}
