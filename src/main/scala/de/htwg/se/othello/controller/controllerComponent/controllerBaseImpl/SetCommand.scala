package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), controller: Controller) extends Command {

  var memento: (BoardInterface, Player) = (controller.boardController.board.deHighlight, controller.getCurrentPlayer)

  override def doStep(): Unit = {
    controller.moves.filter(o => o._2.contains(toSquare)).keys.foreach(fromSquare =>
      controller.boardController.board = controller.boardController.board.flipLine(fromSquare, toSquare, controller.getCurrentPlayer.value).deHighlight)
    // controller.setCurrentPlayer(controller.nextPlayer)
  }

  override def undoStep(): Unit = step()

  override def redoStep(): Unit = step()

  def step(): Unit = {
    val new_memento = (controller.boardController.board.deHighlight, controller.getCurrentPlayer)
    controller.boardController.board = memento._1
    // controller.setCurrentPlayer(memento._2)
    memento = new_memento
  }
}
