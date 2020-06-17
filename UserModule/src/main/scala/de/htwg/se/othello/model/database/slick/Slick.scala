package de.htwg.se.othello.model.database.slick

import de.htwg.se.othello.model.database.Dao
import de.htwg.se.othello.model.{Bot, Player}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class Slick() extends Dao {

  val db = Database.forURL(
    url = "jdbc:mysql://127.0.0.1:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello1",
  )

  val playerTable = TableQuery[PlayerTable]
  val schema = playerTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  def save(currentPlayer: Player, otherPlayer: Player): Unit = {
    Await.result(db.run(DBIO.seq(
      playerTable.insertOrUpdate(currentPlayer.name, currentPlayer.value, currentPlayer.isBot, true),
      playerTable insertOrUpdate(otherPlayer.name, otherPlayer.value, otherPlayer.isBot, false))
    ), Duration.Inf)
  }

  def load(): Vector[Player] = {
    val tableQuery = playerTable.take(2)

    val queryResult = db.run(tableQuery.result)

    val x = Await.result(queryResult, Duration.Inf)
    val currentPlayer = x.filter(pl => pl._4).head
    val otherPlayer = x.filter(pl => !pl._4).head
    Vector(currentPlayer, otherPlayer).map({
      case (name, value, isBot, _)
      => if (isBot) new Bot(name, value) else Player(name, value)
    })
  }
}
