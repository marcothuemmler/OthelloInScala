package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import de.htwg.se.othello.model.databaseComponent.BoardDaoInterface
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import play.api.libs.json.Json
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class BoardDao extends BoardDaoInterface {

  val dbUrl: String = if (sys.env.contains("DOCKER_ENV")) "mysql" else "localhost"

  val db = Database.forURL(
    url = s"jdbc:mysql://$dbUrl:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = sys.env.getOrElse("MYSQL_USER", "root"),
    password = sys.env.getOrElse("MYSQL_ROOT_PASSWORD", "othello1")
  )

  val boardTable = TableQuery[BoardTable]
  val schema = boardTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def save(board: BoardInterface): Unit = {
    val myArray = board.toJson.toString.getBytes
    db.run(DBIO.seq(boardTable.insertOrUpdate(1, myArray))) onComplete {
      case Success(_) => println("Board saved successfully")
      case Failure(error) => println("An error occurred: " + error)
    }
  }

  override def load(): BoardInterface = {
    val tableQuery = boardTable.take(1)

    val queryResult = db.run(tableQuery.result)
    val boardJson = Await.result(queryResult, Duration.Inf).map {
      case (_, boardJson) => Json.parse(boardJson)
    }.head
    (new CreateBoardStrategy).fill(boardJson)
  }
}
