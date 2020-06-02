package de.htwg.se.othello.model.boardComponent

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.htwg.se.othello.model.boardComponent.controller.BoardControllerInterface

import scala.concurrent.{ExecutionContextExecutor, Future}

class BoardModuleHttpServer(controller: BoardControllerInterface) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = ignoreTrailingSlash {
    path("boardMod") {
      toHtml("<h1>BoardModule Webserver</h1>")
    } ~
      path("boardMod" / "resizeBoard") {
        entity(as[ResizeBoardArgumentContainer]) { params =>
          complete(controller.resizeBoard(params.op))
        }
      } ~
      path("boardMod" / "createBoard") {
        entity(as[CreateBoardArgumentContainer]) { params =>
          complete(controller.createBoard(params.size))
        }
      } ~
      path("boardMod" / "boardToString") {
        complete(controller.boardToString)
      } ~
      path("boardMod" / "boardToHtml") {
        complete(controller.boardToHtml)
      }
  }

  def toHtml(html: String): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello - BoardModule</h1>" + html))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind)
      .onComplete(_ => system.terminate)
  }

}
