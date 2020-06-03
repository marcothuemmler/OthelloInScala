package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}

class UserModuleHttpServer(controller: UserControllerInterface) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = ignoreTrailingSlash {
    path("userMod") {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>"))
    } ~
      path("usermodule" / "nextplayer") {
        controller.nextPlayer
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "playercount") {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "" + controller.playerCount))
      } ~
      path("usermodule" / "setupplayers" /Segment) { input =>
        input match {
          case "zero" => controller.setupPlayers("0")
          case "one" => controller.setupPlayers("1")
          case "two" => controller.setupPlayers("2")
        }
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "resetplayer") {
        controller.resetPlayer()
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "getcurrentplayer") {
        controller.getCurrentPlayer
        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(controller.playerToJson)))
      } ~
      path("usermodule" / "getplayers") {
        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(controller.playersToJson)))
      }
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8082)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind)
      .onComplete(_ => system.terminate)
  }

}
