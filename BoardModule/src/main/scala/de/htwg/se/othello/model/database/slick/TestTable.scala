package de.htwg.se.othello.model.database.slick

import slick.jdbc.MySQLProfile.api._

class TestTable(tag: Tag) extends Table[(Int, Int)](tag, "TestTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def x = column[Int]("Zahl")

  def * = (id, x)
}
