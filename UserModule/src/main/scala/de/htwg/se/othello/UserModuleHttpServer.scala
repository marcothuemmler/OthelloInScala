package de.htwg.se.othello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.model.{Bot, Player}
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
        complete(HttpEntity(ContentTypes.`application/json`, controller.nextPlayerToJson.toString))
      } ~
      path("usermodule" / "playercount") {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "" + controller.playerCount))
      } ~
      path("usermodule" / "setupplayers" / Segment) { input =>
        controller.setupPlayers(input)
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "resetplayer") {
        controller.resetPlayer()
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "getcurrentplayer") {
        controller.getCurrentPlayer
        complete(HttpEntity(ContentTypes.`application/json`, controller.playerToJson.toString))
      } ~
      path("usermodule" / "getplayer") {
        parameter(Symbol("isfirstplayer").as[Boolean]) { isFirstPlayer =>
          complete(HttpEntity(ContentTypes.`application/json`, controller.getPlayer(isFirstPlayer).toJson.toString))
        }
      } ~
      path("usermodule" / "getplayers") {
        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(controller.playersToJson)))
      } ~
    path("usermodule" / "setcurrentplayer") {
      parameters(Symbol("name"), Symbol("value").as[Int], Symbol("isBot").as[Boolean]) { (name, value, isBot) =>
        val player = if (isBot) {
          new Bot(name, value)
        } else {
          Player(name, value)
        }
        controller.setCurrentPlayer(player)
        complete(StatusCodes.OK)
      }
    }
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8082)

  def unbind(): Unit = {
    bindingFuture.flatMap(_.unbind).onComplete(_ => system.terminate)
  }
}
