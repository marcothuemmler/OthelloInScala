package de.htwg.se.othello

import de.htwg.se.othello.model.Controller

object Othello {

  def main(args: Array[String]): Unit = {
    println("\nPress \"h\" to highlight possible moves. Press \"q\" to exit")
    val controller = new Controller
    controller.playGame()
    Thread.sleep(550)
    println("\nGoodbye!")
  }
}
