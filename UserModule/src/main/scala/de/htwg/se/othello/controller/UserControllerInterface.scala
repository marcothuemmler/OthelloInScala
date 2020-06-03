package de.htwg.se.othello.controller

import de.htwg.se.othello.model.Player

trait UserControllerInterface {
  def nextPlayer: Player
  def resetPlayer: Unit
  def getPlayer(isFirstPlayer: Boolean): Player
  def getCurrentPlayer: Player
  def setCurrentPlayer(player: Player): Unit
  def playerCount: Int
  def botCount: Int
  def setupPlayers: String => Unit
}
