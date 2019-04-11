package de.htwg.se.othello

import de.htwg.se.othello.model.{Game, MVCRun}

object Othello {

  def main(args: Array[String]): Unit = {
    MVCRun(new Game).playGame()
  }
}
