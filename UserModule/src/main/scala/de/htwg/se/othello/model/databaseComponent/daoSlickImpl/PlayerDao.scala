package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import de.htwg.se.othello.model.databaseComponent.PlayerDaoInterface
import de.htwg.se.othello.model.{Bot, Player}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class PlayerDao extends PlayerDaoInterface {

  val dbUrl: String = if (sys.env.contains("DOCKER_ENV")) "mysql" else "localhost"

  val db = Database.forURL(
    url = s"jdbc:mysql://$dbUrl:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = sys.env.getOrElse("MYSQL_USER", "root"),
    password = sys.env.getOrElse("MYSQL_ROOT_PASSWORD", "othello1")
  )

  val playerTable = TableQuery[PlayerTable]
  val schema = playerTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  def save(currentPlayer: Player, otherPlayer: Player): Unit = {
    db.run(DBIO.seq(
      playerTable.insertOrUpdate(currentPlayer.name, currentPlayer.value, currentPlayer.isBot, true),
      playerTable insertOrUpdate(otherPlayer.name, otherPlayer.value, otherPlayer.isBot, false))
    ) onComplete {
      case Success(_) => println("Players saved successfully")
      case Failure(error) => println("An error occurred: " + error)
    }
  }

  def load(): Vector[Player] = {
    val tableQuery = playerTable.take(2)

    val queryResult = db.run(tableQuery.result)

    Await.result(queryResult, Duration.Inf).map {
      case (name, value, isBot, isCurrentPlayer) =>
        (isCurrentPlayer.compare(true),
          if (isBot) new Bot(name, value) else Player(name, value))
    }.sortBy { case (index, _) => index }
      .map { case (_, player) => player }
      .toVector
  }
}
