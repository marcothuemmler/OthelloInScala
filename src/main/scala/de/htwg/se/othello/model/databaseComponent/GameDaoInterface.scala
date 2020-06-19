package de.htwg.se.othello.model.databaseComponent

trait GameDaoInterface {

  def load(): String
  def save(difficulty: String): Unit

}
