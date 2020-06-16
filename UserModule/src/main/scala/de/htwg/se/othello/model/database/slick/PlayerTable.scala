package de.htwg.se.othello.model.database.slick

import slick.jdbc.MySQLProfile.api._

class PlayerTable(tag: Tag) extends Table[(String, Int, Boolean)](tag, "Player") {

  //def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.PrimaryKey)
  def value = column[Int]("VALUE")
  def isBot = column[Boolean]("ISBOT")

  def * = (name, value, isBot)
}
