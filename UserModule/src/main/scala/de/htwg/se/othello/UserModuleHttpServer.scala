//package de.htwg.se.othello
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.Http
//import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.{Route, StandardRoute}
//import de.htwg.se.othello.controller.UserControllerInterface
//
//import scala.concurrent.{ExecutionContextExecutor, Future}
//
//class UserModuleHttpServerHttpServer(controller: UserControllerInterface) {
//
//  implicit val system: ActorSystem = ActorSystem("my-system")
//  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
//
//  val route: Route = ignoreTrailingSlash {
//    path("userMod") {
//      toHtml("<h1>UserModule Webserver</h1>")
//    } ~
//      path("userMod" / "nextPlayer") {
//        complete(controller.nextPlayer)
//      } ~
//      path("userMod" / "playerCount") {
//        complete(controller.playerCount)
//      } ~
//      path("userMod" / "setupPlayers") {
//        complete(controller.setupPlayers)
//      } ~
//      path("userMod" / "resetPlayer") {
//        complete(controller.resetPlayer)
//      } ~
//      path("userMod" / "getCurrentPlayer") {
//        complete(controller.getCurrentPlayer)
//      } /*~
//      path("othello" / "getPlayers") {
//        controller.getPlayer
//        gridtoHtml
//      } ~
//      path("othello" / "setCurrentPlayer") {
//        controller.setCurrentPlayer()
//        gridtoHtml
//      }*/
//  }
//
//  def toHtml(html: String): StandardRoute = {
//    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello - UserModule</h1>" + html))
//  }
//
//  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)
//
//  def unbind(): Unit = {
//    bindingFuture
//      .flatMap(_.unbind)
//      .onComplete(_ => system.terminate)
//  }
//
//}
