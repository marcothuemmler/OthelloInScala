package de.htwg.se.othello

import de.htwg.se.othello.model.{Bot, MVCRun, Player}

object Othello {

  def main(args: Array[String]): Unit = {
    val mvc = new MVCRun
    mvc.playGame(Vector(
      new Player(1, mvc.game),
      new Bot(2, mvc.game)))
  }
}
