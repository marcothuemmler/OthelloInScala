package de.htwg.se.othello.controller

import de.htwg.se.othello.model.Board
import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), value: Int, controller: Controller) extends Command {

  var memento: Board = controller.board.deHighlight

  override def doStep(): Unit = {
    memento = controller.board.deHighlight
    if (controller.moves.isEmpty) {
      controller.player = controller.nextPlayer
      controller.gameStatus = GameStatus.OMITTED
    } else {
      val legal = controller.moves.filter(o => o._2.contains(toSquare))
      if (legal.isEmpty) controller.gameStatus = GameStatus.ILLEGAL
      else {
        for {
          fromSquare <- legal.keys
        } controller.board = controller.board.flipLine(fromSquare, toSquare, value).deHighlight
        controller.player = controller.nextPlayer
      }
    }
    if (controller.board.gameOver) controller.gameStatus = GameStatus.GAME_OVER
  }

  override def undoStep(): Unit = {
    val new_memento = controller.board.deHighlight
    controller.board = memento
    memento = new_memento
  }

  override def redoStep(): Unit = {
    val new_memento = controller.board.deHighlight
    controller.board = memento
    memento = new_memento
  }
}
