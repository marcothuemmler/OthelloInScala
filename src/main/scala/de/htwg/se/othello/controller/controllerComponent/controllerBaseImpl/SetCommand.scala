package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Board
import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), value: Int, controller: Controller) extends Command {

  var memento: Board = controller.board.deHighlight

  override def doStep(): Unit = {
    for {
      fromSquare <- controller.moves.filter(o => o._2.contains(toSquare)).keys
    } controller.board = controller.board.flipLine(fromSquare, toSquare, value).deHighlight
    controller.player = controller.nextPlayer
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
