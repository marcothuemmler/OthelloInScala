package de.htwg.se.othello

import de.htwg.se.othello.model.{Board, Game, Player}

import scala.io.StdIn
import scala.util.Random

object Othello {

  def main(args: Array[String]): Unit = {
    val game = Game(new Board)
    var input = ""
    println("Please input name")
    val player1 = new Player(StdIn.readLine(), 1, game)
    val player2 = new Player("bot", 2, game)
    val players = Array[Player](player1, player2)
    var i = 0
    var x: Int = 0
    var y: Int = 0
    while (input != "q") {
      val currentPlayer = players(i)
      if (i == 0) {
        currentPlayer.highlight()
        input = StdIn.readLine()
        val tokens = input.split(" ")
        x = tokens(0).toInt
        y = tokens(1).toInt
      } else {
        val tile = getRandomFirst(currentPlayer.moves().filter(_._2.nonEmpty))
        val move = getRandomSecond(tile._2)
        x = move._1
        y = move._2
        Thread.sleep(1000)
      }
      if (currentPlayer.set(x, y)) {
        if (i + 1 == players.length) i = 0 else i = 1
        game.update()
      }
    }
  }

  def getRandomFirst(map: Map[(Int, Int), List[(Int, Int)]]): ((Int, Int), List[(Int, Int)]) = {
    val r = Random
    map.toList(r.nextInt(map.keys.size))
  }

  def getRandomSecond(list: List[(Int, Int)]): (Int, Int) = {
    val r = Random
    list(r.nextInt(list.size))
  }
}
