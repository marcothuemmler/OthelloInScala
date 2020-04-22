package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), value: Int, controller: Controller) extends Command {

  var memento: (BoardInterface, Player) = (controller.board.deHighlight, controller.player)

  override def doStep(): Unit = {
    controller.moves.filter(o => o._2.contains(toSquare)).keys.foreach(fromSquare =>
      controller.board = controller.board.flipLine(fromSquare, toSquare, value).deHighlight)
    controller.player = controller.nextPlayer
  }

  override def undoStep(): Unit = step()

  override def redoStep(): Unit = step()

  def step(): Unit = {
    val new_memento = (controller.board.deHighlight, controller.player)
    controller.board = memento._1
    controller.player = memento._2
    memento = new_memento
  }
}
