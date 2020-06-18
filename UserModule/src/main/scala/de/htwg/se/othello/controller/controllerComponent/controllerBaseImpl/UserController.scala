package de.htwg.se.othello.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Injector}
import de.htwg.se.othello.UserModule
import de.htwg.se.othello.controller.controllerComponent.UserControllerInterface
import de.htwg.se.othello.model.databaseComponent.PlayerDaoInterface
import de.htwg.se.othello.model.{Bot, Player}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.json.{JsObject, Json}

class UserController extends UserControllerInterface {

  var players: Vector[Player] = Vector(new Player(1), new Bot(2))
  var player: Player = players(0)

  val injector: Injector = Guice.createInjector(new UserModule)
  val dao: PlayerDaoInterface = injector.instance[PlayerDaoInterface]

  def nextPlayer: Player = if (player == players(0)) players(1) else players(0)

  def playerCount: Int = players.count(o => !o.isBot)

  def setupPlayers: String => Unit = {
    case "0" => players = Vector(new Bot(1), new Bot(2))
    case "1" => players = Vector(new Player(1), new Bot(2))
    case "2" => players = Vector(new Player(1), new Player(2))
  }

  def resetPlayer(): Unit = player = players(0)

  def getPlayer(isFirstPlayer: Boolean): Player = if (isFirstPlayer) players(0) else players(1)

  def setCurrentPlayer(player: Player): Unit = this.player = player

  def playerToJson: JsObject = player.toJson

  def nextPlayerToJson: JsObject = nextPlayer.toJson

  def playersToJson: JsObject = Json.obj("players" -> players.map(p => p.toJson))

  override def save(): Unit = dao.save(player, nextPlayer)

  override def load(): Unit = {
    val loadedPlayers = dao.load()
    player = loadedPlayers(0)
    players = loadedPlayers.sortBy(_.value)
  }
}
