package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.controllerComponent._

import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)

  def processInputLine: String => Unit = {
    case "q" => sys.exit
    case "n" => controller.newGame
    case "h" => controller.highlight()
    case "z" => controller.undo()
    case "y" => controller.redo()
    case "s" => println(controller.suggestions)
    case input @ ("e" | "m" | "d") => controller.setDifficulty(input)
    case input @ ("+" | "-" | ".") => controller.resizeBoard(input)
    case input @ ("0" | "1" | "2") => controller.setupPlayers(input)
    case input => input.toList match {
      case col :: row :: Nil =>
        val square = (col.toUpper.toInt - 65, row.asDigit - 1)
        controller.set(square)
      case _ => println("Please try again. " + controller.suggestions)
    }
  }

  reactions += { case _: BoardChanged | _: PlayerOmitted => update }

  def update: Boolean = {
    if (!controller.gameOver) {
      println(GameStatus.message(controller.gameStatus))
      println(controller.boardToString)
    } else {
      println(controller.boardToString + "\n" + controller.score)
      println(GameStatus.message(controller.gameStatus))
    }
    controller.gameStatus = GameStatus.IDLE
    true
  }
}
