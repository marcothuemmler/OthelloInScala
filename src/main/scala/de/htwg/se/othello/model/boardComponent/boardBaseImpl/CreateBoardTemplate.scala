package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.OthelloModule
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

trait CreateBoardTemplate {

  val injector: Injector = Guice.createInjector(new OthelloModule)

  def createNewBoard(size: Int): BoardInterface = {
    var board: BoardInterface = injector.instance[BoardFactory].create(size)
    board = prepare(board)
    board = fill(board)
    postProcess(board)
  }

  def prepare(board: BoardInterface): BoardInterface = board

  def fill(board: BoardInterface): BoardInterface

  def postProcess(board: BoardInterface): BoardInterface = board
}
