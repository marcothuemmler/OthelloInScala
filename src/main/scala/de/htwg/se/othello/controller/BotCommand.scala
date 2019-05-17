package de.htwg.se.othello.controller

import de.htwg.se.othello.model.Board
import de.htwg.se.othello.util.Command

class BotCommand(controller : Controller) extends Command {

  val botBoard: Board = controller.board.deHighlight
  val oldBoard: Board = controller.board

  override def doStep(): Unit = controller.board = botBoard

  override def undoStep(): Unit = {
    controller.player = controller.nextPlayer
    controller.board = oldBoard
  }

  override def redoStep(): Unit = {
    controller.player = controller.nextPlayer
    controller.board = botBoard
  }
}
