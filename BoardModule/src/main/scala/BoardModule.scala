import de.htwg.se.othello.model.boardComponent.BoardModuleHttpServer
import de.htwg.se.othello.model.boardComponent.controller.controllerBaseImpl.BoardController

object BoardModule {

  def main(args: Array[String]): Unit = {
    new BoardModuleHttpServer(new BoardController)
  }
}
