
package de.htwg.se.othello.controller

import de.htwg.se.othello.model.Board
import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), value: Int, controller: Controller) extends Command {

  var memento: Board = controller.board

  override def doStep(): Unit = {
    memento = controller.board
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
  }

  override def undoStep(): Unit = {
    val new_memento = controller.board
    controller.board = memento
    memento = new_memento
  }

  override def redoStep(): Unit = {
    val new_memento = controller.board
    controller.board = memento
    memento = new_memento
  }
}
