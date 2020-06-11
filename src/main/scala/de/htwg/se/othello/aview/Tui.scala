package de.htwg.se.othello.aview

import de.htwg.se.othello.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.othello.controller.controllerComponent.GameStatus._
import de.htwg.se.othello.util.Observer

class Tui(controller: ControllerInterface) extends Observer {

  controller.add(this)

  def processInputLine: String => Unit = {
    case "q" => sys.exit
    case "n" => controller.newGame
    case "h" => controller.highlight()
    case "z" => controller.undo()
    case "y" => controller.redo()
    case "s" => println(controller.suggestions)
    case "f" => controller.save("savegame.xml")
    case "l" => controller.load("savegame.xml")
    case input@("e" | "m" | "d") => controller.setDifficulty(input)
    case input@("+" | "-" | ".") => controller.resizeBoard(input)
    case input@("0" | "1" | "2") => controller.setupPlayers(input)
    case input => input.toList match {
      case col :: row :: Nil =>
        val square = (col.toUpper.toInt - 65, row.asDigit - 1)
        controller.set(square)
      case _ => controller.illegalAction()
    }
  }

  def update: Boolean = {
    controller.gameStatus match {
      case IDLE => println(controller.boardToString)
      case GAME_OVER =>
        println(controller.boardToString)
        println(controller.score)
        println(GameStatus.message(controller.gameStatus))
      case LOAD_SUCCESS =>
        println(GameStatus.message(controller.gameStatus))
        println(controller.boardToString)
      case OMITTED =>
        print(GameStatus.message(controller.gameStatus))
        println(" for " + controller.nextPlayer)
        println(controller.boardToString)
      case ILLEGAL =>
        print(GameStatus.message(controller.gameStatus))
        println(". Possible moves: " + controller.suggestions)
        println(controller.boardToString)
      case _ => println(GameStatus.message(controller.gameStatus))
    }
    true
  }
}
