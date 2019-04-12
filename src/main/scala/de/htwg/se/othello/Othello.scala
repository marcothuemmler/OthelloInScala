package de.htwg.se.othello

import de.htwg.se.othello.model.MVCRun

object Othello {

  def main(args: Array[String]): Unit = {
    val mvc = MVCRun()
    val players = Array(mvc.player(1),
                        mvc.player("bot", 2))
    mvc.playGame(players)
  }
}
