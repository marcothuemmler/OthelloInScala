package de.htwg.se.othello.controller.controllerComponent

import de.htwg.se.othello.model.Player
import play.api.libs.json.JsObject

trait UserControllerInterface {
  def nextPlayer: Player
  def resetPlayer(): Unit
  def getPlayer(isFirstPlayer: Boolean): Player
  def getCurrentPlayer: Player
  def setCurrentPlayer(player: Player): Unit
  def playerCount: Int
  def setupPlayers: String => Unit
  def playerToJson: JsObject
  def nextPlayerToJson: JsObject
  def playersToJson: JsObject
}
