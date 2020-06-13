package de.htwg.se.othello.model.database.slick

import de.htwg.se.othello.model.Player
import de.htwg.se.othello.model.database.Dao
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.util.Try

case class Slick() extends Dao {

  val db = Database.forURL(
    url = "jdbc:mysql://localhost:3306",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "root",
    password = "othello"
  )

  val schema = TableQuery[PlayerTable].schema

  db.run(DBIO.seq(schema.createIfNotExists))

  override def create(player: Player): Unit = ???

  override def read(): Unit = ???

  override def update(player: Player): Unit = ???

  override def delete(player: Player): Unit = ???
}
