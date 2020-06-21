package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import de.htwg.se.othello.model.databaseComponent.BoardDaoInterface
import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import play.api.libs.json.Json
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class BoardDao extends BoardDaoInterface {

  val dbUrl: String = if (sys.env.contains("DOCKER_ENV")) "mysql" else "localhost"

  val db = Database.forURL(
    url = s"jdbc:mysql://$dbUrl:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello1"
  )

  val boardTable = TableQuery[BoardTable]
  val schema = boardTable.schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def save(board: BoardInterface): Unit = {
    val myArray: Array[Byte] = board.toJson.toString.getBytes
    val id: Int = 1
    db.run(DBIO.seq(boardTable.insertOrUpdate(id, myArray)))
  }

  override def load(): BoardInterface = {
    val tableQuery = boardTable.take(1).sortBy(_.id)

    val queryResult = db.run(tableQuery.result)
    val boardJson = Await.result(queryResult, Duration.Inf).reverse.map {
      case (_, boardJson) => Json.parse(boardJson)
    }.head
    (new CreateBoardStrategy).fill(boardJson)
  }
}
