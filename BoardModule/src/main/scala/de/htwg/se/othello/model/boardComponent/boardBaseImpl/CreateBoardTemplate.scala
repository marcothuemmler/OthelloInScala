package de.htwg.se.othello.model.boardComponent.boardBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.BoardModule
import de.htwg.se.othello.model.boardComponent.{BoardFactory, BoardInterface}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.json.JsValue

trait CreateBoardTemplate {

  val injector: Injector = Guice.createInjector(new BoardModule)

  def createNewBoard(size: Int): BoardInterface = {
    postProcess(fill(prepare(injector.instance[BoardFactory].create(size))))
  }

  def prepare(board: BoardInterface): BoardInterface = board

  def fill(board: BoardInterface): BoardInterface

  def fill(json: JsValue): BoardInterface

  def postProcess(board: BoardInterface): BoardInterface = board
}
