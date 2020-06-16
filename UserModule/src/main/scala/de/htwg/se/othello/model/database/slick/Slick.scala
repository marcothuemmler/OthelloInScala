package de.htwg.se.othello.model.database.slick

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.database.Dao
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class Slick() extends Dao {

  val db = Database.forURL(
    url = "jdbc:mysql://127.0.0.1:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello",
  )

  val playerTable = TableQuery[PlayerTable]
  val schema = playerTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def save(players: Vector[Player]): Unit = {
    players.foreach { player =>
      Await.ready(db.run((playerTable += (player.name, player.value, player.isBot))), Duration.Inf)
    }
  }

  override def load(): Vector[Player] = {
    val tableQuery = playerTable.take(1).result.head
    val queryResult = db.run(tableQuery)
   // val x = queryResult.
    //new Player(queryResult.)
    val x: Vector[Player] = Vector(new Player(1))
    x
  }

}
