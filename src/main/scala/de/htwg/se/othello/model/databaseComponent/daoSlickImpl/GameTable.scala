package de.htwg.se.othello.model.databaseComponent.daoSlickImpl

import slick.jdbc.MySQLProfile.api._

class GameTable(tag: Tag) extends Table[(Int, String)](tag, "difficulty") {

  def id = column[Int]("id", O.PrimaryKey)
  def difficulty = column[String]("difficulty")
  override def * = (id, difficulty)
}
