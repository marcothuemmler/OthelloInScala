package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import de.htwg.se.othello.model.databaseComponent.GameDaoInterface
import slick.lifted.TableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameDao extends GameDaoInterface {

  val db = Database.forURL(
    url = "jdbc:mysql://127.0.0.1:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello1"
  )

  val gameTable = TableQuery[GameTable]
  val schema = gameTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def load(): String = {

    val tableQuery = gameTable.take(1)
    val queryResult = db.run(tableQuery.result)
    val result = Await.result(queryResult, Duration.Inf)
    result.map({case (_, difficulty) => difficulty}).head
  }

  override def save(difficulty: String): Unit = db.run(gameTable += (1, difficulty))
}
