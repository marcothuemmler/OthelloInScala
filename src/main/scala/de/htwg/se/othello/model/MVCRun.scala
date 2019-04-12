package de.htwg.se.othello.model

import scala.io.StdIn

case class MVCRun() {

  val game: Game = Game(new Board)

  def playGame(players: Array[Player]): Unit = {
    var x, y = -1
    var i = 0
    while (players(0).moves.nonEmpty || players(1).moves.nonEmpty) {
      if (players(i).moves.isEmpty) {
        print(f"No possible moves for ${players(i)}. ")
        i = if (i == 1) 0 else 1
        println(f"${players(i)}'s turn")
      }
      players(i) match {
        case bot: Bot =>
          val move = bot.getMove
          x = move._1
          y = move._2
        case _ =>
          players(i).highlight()
          game.update()
          val input = StdIn.readLine
          if (input == "q") return
          val tokens = input
          if (tokens.length == 2) {
            val tile = tokens.split(" ")
            x = tile(0).toInt
            y = tile(1).toInt
          }
      }
      if (!players(i).set(x, y)) {
        println(f"Please try again. Possible moves for ${players(i)}:")
        val possible = players(i).moves.values.flatten.toSet
        for (option <- possible) {
          print(f"$option ")
        }
        println
      } else {
        i = if (i == 1) 0 else 1
        game.update()
      }
    }
    winner(players)
  }

  def winner(p: Array[Player]): Unit = {
    val winner = if (p(0).count > p(1).count) p(0) else p(1)
    val loser = if (winner == p(0)) p(1) else p(0)
    if (p(0).count != p(1).count) {
      println(s"$winner wins by ${winner.count}:${loser.count}!")
    } else {
      println(s"Draw. ${p(0).count}:${p(1).count}")
    }
  }

  def player(kind: String, value: Int): Player = {
    if (kind == "bot") new Bot(value, game) else new Player(value, game)
  }
  def player(value: Int): Player = new Player(value, game)
}
