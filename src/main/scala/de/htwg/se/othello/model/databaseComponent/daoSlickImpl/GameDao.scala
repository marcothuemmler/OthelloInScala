package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import de.htwg.se.othello.model.databaseComponent.GameDaoInterface
import slick.lifted.TableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GameDao extends GameDaoInterface {

  val dbUrl: String = if (sys.env.contains("DOCKER_ENV")) "mysql" else "localhost"

  val db = Database.forURL(
    url = s"jdbc:mysql://$dbUrl:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = sys.env.getOrElse("MYSQL_USER", "root"),
    password = sys.env.getOrElse("MYSQL_ROOT_PASSWORD", "othello1")
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

  override def save(difficulty: String): Unit = {
    db.run(gameTable.insertOrUpdate(1, difficulty)) onComplete {
      case Success(_) => println("Difficulty saved successfully")
      case Failure(error) => println("An error occurred: " + error)
    }
  }
}
