package de.htwg.se.othello.model.database.slick

import de.htwg.se.othello.model._
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

  def save(currentPlayer: Player, otherPlayer: Player): Unit = {
    try {
      Await.result(db.run(DBIO.seq(
        playerTable.insertOrUpdate(currentPlayer.name, currentPlayer.value, currentPlayer.isBot),
        playerTable insertOrUpdate  (otherPlayer.name, otherPlayer.value, otherPlayer.isBot))
      ), Duration.Inf)
    }
    catch {
      case err: Exception =>
        println("Error in database", err)

    }
  }

  def load(): Vector[Player] = {
    val tableQuery = playerTable.take(2)
//
//    var test = Await.result(db.run(tableQuery.result.map(pl => {
//
//      val name = pl.head._1
//      val value = pl.head._2
//      val isBot = pl.head._3
//      println(name)
//      if (isBot) new Bot(value) else new Player(value)
//    })(ExecutionContext.global)), Duration.Inf)
//
//
    val queryResult = db.run(tableQuery.result)

    val x = Await.result(queryResult, Duration.Inf).map({case (name, value, isBot)
    =>
      if (isBot) new Bot(value) else new Player(value)})
    x.toVector
//    val tableQuery = playerTable.take(1).result.head
//    val queryResult = Await.result(db.run(tableQuery), Duration.Inf)
//    val player = new Player(queryResult._2)
//    val x: Vector[Player] = Vector(player)
    //x
  }

}
