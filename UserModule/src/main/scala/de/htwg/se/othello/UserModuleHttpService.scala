package de.htwg.se.othello

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.model.Player
import play.api.libs.json.Json

trait UserModuleHttpService {

  implicit val controller: UserControllerInterface

  val route: Route = ignoreTrailingSlash {
    path("usermodule") {
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
        entity(as[String]) { content =>
          val player = Player.fromJson(Json.parse(content))
          controller.setCurrentPlayer(player)
          complete(StatusCodes.OK)
        }
      } ~
      path("usermodule" / "setplayername") {
        parameter(Symbol("index").as[Int], Symbol("name")) { (index, name) =>
          controller.setPlayerName(index, name)
          complete(StatusCodes.OK)
        }
      } ~
      path("usermodule" / "save") {
        controller.save()
        complete(StatusCodes.OK)
      } ~
      path("usermodule" / "load") {
        controller.load()
        complete(StatusCodes.OK)
      }
  }
}
