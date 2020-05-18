package boardComponent.boardBaseImpl

import boardComponent.BoardInterface

trait CreateBoardTemplate {

  //val injector: Injector = Guice.createInjector(new OthelloModule)

  def createNewBoard(size: Int): BoardInterface = {
    postProcess(fill(size))

  }

  def prepare(board: BoardInterface): BoardInterface = board

  def fill(size: Int): BoardInterface

  def postProcess(board: BoardInterface): BoardInterface = board
}
