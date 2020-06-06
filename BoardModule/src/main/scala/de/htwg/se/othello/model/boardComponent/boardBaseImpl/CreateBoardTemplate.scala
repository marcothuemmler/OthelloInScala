package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.BoardModuleModule
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

trait CreateBoardTemplate {

  val injector: Injector = Guice.createInjector(new BoardModuleModule)

  def createNewBoard(size: Int): BoardInterface = {
    postProcess(fill(prepare(injector.instance[BoardFactory].create(size))))
  }

  def prepare(board: BoardInterface): BoardInterface = board

  def fill(board: BoardInterface): BoardInterface

  def postProcess(board: BoardInterface): BoardInterface = board
}
