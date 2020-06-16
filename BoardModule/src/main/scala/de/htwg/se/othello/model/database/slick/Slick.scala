package de.htwg.se.othello.model.database.slick

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.database.Dao
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

case class Slick() extends Dao {

  val db = Database.forURL(
    url = "jdbc:mysql://127.0.0.1:3306/othello?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello"
  )

  val schema = TableQuery[TestTable].schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def read(board: BoardInterface): Unit = ???

  override def create(board: BoardInterface): Unit = ???

  override def update(board: BoardInterface): Unit = ???

  override def delete(board: BoardInterface): Unit = ???
}
