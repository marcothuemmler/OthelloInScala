package de.htwg.se.othello.model

import scala.io.StdIn

case class MVCRun(game: Game) {

  def playGame(): Unit = {
    val players = playerSelect
    var x, y = -1
    var i = 0
    while (players(0).moves.nonEmpty || players(1).moves.nonEmpty) {
      val currentPlayer = players(i)
      if (currentPlayer.moves.isEmpty) {
        print(f"No possible moves for $currentPlayer. ")
        i = if (i == 1) 0 else 1
        println(f"${players(i)}'s turn")
      }
      currentPlayer match {
        case bot: Bot =>
          val move = bot.getMove
          x = move._1
          y = move._2
          Thread.sleep(500)
        case _ =>
          currentPlayer.highlight()
          game.update()
          val input = StdIn.readLine
          if (input == "q") return
          val tokens = input.split(" ")
          if (tokens.length == 2) {
            x = tokens(0).toInt
            y = tokens(1).toInt
          }
      }
      if (currentPlayer.set(x, y)) {
        i = if (i == 1) 0 else 1
        game.update()
      } else {
        println(f"Please try again. Possible moves for $currentPlayer}:")
        val possible = currentPlayer.moves.values.flatten.toSet
        for (option <- possible) {
          print(f"$option ")
        }
        println
      }
    }
    println(winner(players(0), players(1)))
  }

  def winner(p1: Player, p2: Player): String = {
    val winner = if (p1.count > p2.count) p1 else p2
    val loser = if (winner == p1) p2 else p1
    if (p1.count != p2.count) {
      s"$winner wins by ${winner.count}:${loser.count}!"
    } else {
      s"Draw. ${p1.count}:${p2.count}"
    }
  }

  def playerSelect: Array[Player] = {
    Array.tabulate(2)(i =>
      if (StdIn.readLine(s"Please input in player${i + 1} type. " +
        "Type bot for computer player\n") == "bot") {
        new Bot(s"Bot${i + 1}", i + 1, game)
      } else {
        new Player(StdIn.readLine("Please input your name:\n"), i + 1, game)
      }
    )
  }
}
