package de.htwg.se.othello.model.database.slick

import slick.jdbc.MySQLProfile.api._

case class BoardTable(tag: Tag) extends Table[(Int, Array[Byte])](tag, "board") {

  def id = column[Int]("id", O.PrimaryKey)

  def board = column[Array[Byte]]("board")

  def * = (id, board)
}
