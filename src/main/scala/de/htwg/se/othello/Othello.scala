package de.htwg.se.othello

import de.htwg.se.othello.model.{Board, Game, Player, Bot}

import scala.io.StdIn

object Othello {

  def main(args: Array[String]): Unit = {
    val game = Game(new Board)
    val p1 = if (StdIn.readLine == "human") {
      new Player("Player1", 1, game)
    } else {
      new Bot("Bot1", 1, game)
    }
    val p2 = if (StdIn.readLine == "human") {
      new Player("Player2", 2, game)
    } else {
      new Bot("Bot2", 2, game)
    }
    val players = Array(p1, p2)
    var i = 0
    var x, y = -1
    while (players(0).moves.nonEmpty || players(1).moves.nonEmpty) {
      if (players(i).moves.isEmpty) {
        print(f"No possible moves for ${players(i)}. ")
        i = if (i == 1) 0 else 1
        println(f"${players(i)}'s turn")
      }
      players(i) match {
        case bot: Bot =>
          val move = bot.getMove(players(i).moves)
          x = move._1
          y = move._2
          Thread.sleep(500)
        case _ =>
          players(i).highlight()
          game.update()
          val input = StdIn.readLine
          if (input == "q") return
          val tokens = input.split(" ")
          if (tokens.length == 2) {
            x = tokens(0).toInt
            y = tokens(1).toInt
          }
      }
      if (players(i).set(x, y)) {
        i = if (i == 1) 0 else 1
        game.update()
      } else {
        println(f"Please try again. Possible moves for ${players(i)}:")
        val possible = players(i).moves.values.flatten.toSet
        for (move <- possible) {
          print(f"$move ")
        }
        println
      }
    }
    declareWinner(players(0), players(1))
  }

  def declareWinner(p1: Player, p2: Player): Unit = {
    val winner = if (p1.count > p2.count) p1 else p2
    val loser = if (winner == p1) p2 else p1
    if (p1.count != p2.count) {
      println(f"$winner wins by ${winner.count}:${loser.count}!")
    } else {
      println(f"Draw. ${p1.count}:${p2.count}")
    }
  }
}
