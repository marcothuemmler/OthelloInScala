package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.util.Command

case class SetCommand(toSquare: (Int, Int))(implicit val controller: Controller) extends Command {

  var memento: (BoardInterface, Player) = (controller.getBoard.deHighlight, controller.getCurrentPlayer)

  override def doStep(): Unit = {
    controller.moves.filter(o => o._2.contains(toSquare)).keys.foreach(fromSquare =>
      controller.setBoard(controller.getBoard.flipLine(fromSquare, toSquare, controller.getCurrentPlayer.value).deHighlight))
      controller.setCurrentPlayer(controller.nextPlayer)
  }

  override def undoStep(): Unit = step()

  override def redoStep(): Unit = step()

  def step(): Unit = {
    val new_memento = (controller.getBoard.deHighlight, controller.getCurrentPlayer)
    controller.setBoard(memento._1)
    controller.setCurrentPlayer(memento._2)
    memento = new_memento
  }
}
