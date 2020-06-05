package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.htwg.se.othello.controller.controllerComponent.BoardControllerInterface
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}

class BoardModuleHttpServer(controller: BoardControllerInterface) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = ignoreTrailingSlash {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>"))
    } ~
    path("boardmodule") {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>"))
    } ~
    path("boardmodule" / "resize") {
      parameter('op) { op =>
        controller.resizeBoard(op)
        complete(StatusCodes.OK)
      }
    } ~
    path("boardmodule" / "size") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "" + controller.size))
    } ~
    path("boardmodule" / "boardjson") {
      complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(controller.toJson)))
    } ~
    path("boardmodule" / "boardstring") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, controller.boardToString))
    } ~
    path("boardmodule" / "boardhtml") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, controller.boardToHtml))
    } ~
    path("boardmodule" / "gameover") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, controller.gameOver.toString))
    } ~
    path("boardmodule" / "changehighlight") {
      parameter('value) { value =>
        controller.changeHighlight(value.toInt)
        complete(StatusCodes.OK)
      }
    } ~
    path("boardmodule" / "valueof") {
      parameter('col, 'row) { (col, row) =>
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, controller.valueOf(col.toInt, row.toInt).toString))
      }
    } ~
    path("boardmodule" / "moves") {
      parameter('value) { value =>
        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(controller.movesToJson(value.toInt))))
      }
    } ~
    path("boardmodule" / "create") {
      parameter('size) { size =>
        controller.createBoard(size.toInt)
        complete(StatusCodes.OK)
      }
    } ~
    path("boardmodule" / "set") {
        entity(as[String]) { content: String =>
          val boardJson = Json.parse(content)
          val size = controller.size
          var board = controller.board
          for {
            index <- 0 until size * size
            row = (boardJson \\ "row") (index).as[Int]
            col = (boardJson \\ "col") (index).as[Int]
            value = (boardJson \\ "value") (index).as[Int]
          } board = board.flipLine((row, col), (row, col), value)
          controller.board = board
          complete(StatusCodes.OK)
        }
    } ~
    path("boardmodule" / "count") {
      parameter('value) { value =>
        complete(HttpEntity(ContentTypes.`application/json`, controller.count(value.toInt).toString))
      }
    }
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8081)

  def unbind(): Unit = {
    bindingFuture.flatMap(_.unbind).onComplete(_ => system.terminate)
  }
}
