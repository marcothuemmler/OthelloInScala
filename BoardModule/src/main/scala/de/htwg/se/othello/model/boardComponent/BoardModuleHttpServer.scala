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
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>"))
    } ~
    path("boardmod") {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>"))
    } ~
      path("boardmod" / "resize" / Segment) { input =>
        input match {
          case "increase" =>
            controller.resizeBoard("+")
          case "decrease" =>
            controller.resizeBoard("-")
          case "reset" =>
            controller.resizeBoard(".")
        }
        boardSize
      } ~
    path("boardmod" / "getsize") {
      boardSize
    }
  }

  def boardSize: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "" + controller.size))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8081)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind)
      .onComplete(_ => system.terminate)
  }

}
