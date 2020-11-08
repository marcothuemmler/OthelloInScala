package de.htwg.se.othello.aview

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.htwg.se.othello.controller.controllerComponent.ControllerInterface

trait OthelloHttpService {

  val controller: ControllerInterface

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
      path("othello" / "highlight") {
        controller.highlight()
        gridtoHtml
      } ~
      path("othello" / "save") {
        controller.save(None)
        gridtoHtml
      } ~
      path("othello" / "load") {
        controller.load(None)
        gridtoHtml
      } ~
      path("othello" / "board" / "increase") {
        controller.resizeBoard("+")
        gridtoHtml
      } ~
      path("othello" / "board" / "reduce") {
        controller.resizeBoard("-")
        gridtoHtml
      } ~
      path("othello" / "board" / "reset") {
        controller.resizeBoard(".")
        gridtoHtml
      } ~
      path("othello" / "difficulty" / Segment) {command => {
        controller.setDifficulty(command)
        gridtoHtml
      }} ~
      path("othello" / "players" / Segment) {command => {
        controller.setupPlayers(command)
        gridtoHtml
      }} ~
      path("othello" / Segment) { command => {
        processInputLine(command)
        gridtoHtml
      }}
  }

  def gridtoHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Othello</h1>" + controller.boardToHtml))
  }

  def processInputLine: String => Unit = {
    input => input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)").toList match {
      case col :: row :: Nil =>
        val square = (col.charAt(0).toUpper - 65, row.toInt- 1)
        controller.set(square)
      case _ =>
    }
  }

}