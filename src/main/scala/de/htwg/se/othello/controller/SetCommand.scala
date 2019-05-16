package de.htwg.se.othello.controller

import de.htwg.se.othello.util.Command

class SetCommand(toSquare: (Int, Int), value: Int, controller: Controller) extends Command {

  override def doStep(): Unit = {
    if (controller.moves.nonEmpty) {
      val legal = controller.moves.filter(o => o._2.contains(toSquare))
      if (legal.isEmpty) controller.gameStatus = GameStatus.ILLEGAL
      for {
        fromSquare <- legal.keys
      } controller.board = controller.board.flipLine(fromSquare, toSquare, value)
      controller.board = controller.board.deHighlight
      if (controller.gameStatus != GameStatus.ILLEGAL) {
        controller.player = controller.nextPlayer
      }
    } else {
      controller.player = controller.nextPlayer
      controller.gameStatus = GameStatus.OMITTED
    }
    if (controller.board.gameOver) controller.gameStatus = GameStatus.GAME_OVER
  }

  override def undoStep(): Unit = {
    controller.board = controller.boardList(controller.boardList.length - 2).deHighlight
  }

  override def redoStep(): Unit = {
    controller.board = controller.boardList.last
    controller.player = controller.nextPlayer
    val legal = controller.moves.filter(o => o._2.contains(toSquare))
    for {
      fromSquare <- legal.keys
    } controller.board = controller.board.flipLine(fromSquare, toSquare, value)
    controller.board = controller.board.deHighlight
    controller.player = controller.nextPlayer
  }
}
