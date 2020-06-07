package de.htwg.se.othello.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface

import scala.concurrent.{ExecutionContextExecutor, Future}

class HttpServer(controller: ControllerInterface) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = ignoreTrailingSlash {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Othello</h1>"))
    }
    path("othello") {
      gridtoHtml
    } ~
      path("othello" / "new") {
        controller.newGame
        gridtoHtml
      } ~
      path("othello" / "undo") {
        controller.undo()
        gridtoHtml
      } ~
      path("othello" / "redo") {
        controller.redo()
        gridtoHtml
      } ~
      path("othello" / Segment) { command => {
        processInputLine(command)
        gridtoHtml
      }}
  }

  def gridtoHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>" + controller.boardToHtml))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8080)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind)
      .onComplete(_ => system.terminate)
  }

  def processInputLine: String => Unit = {
    input => input.toList match {
      case col :: row :: Nil =>
        val square = (col.toUpper.toInt - 65, row.asDigit - 1)
        controller.set(square)
      case _ =>
    }
  }

}