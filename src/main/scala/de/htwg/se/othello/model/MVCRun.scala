package de.htwg.se.othello.model

import scala.io.StdIn.readLine

case class MVCRun() {

  val game = new Game

  def playGame(players: Vector[Player]): Unit = {
    var x, y = -1
    var i = 0
    var in = ""
    game.update()
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
          Thread.sleep(750)
          println(f"${players(i)} sets ${mapOutput(x)}${y + 1}")
        case _ =>
          in = readLine
          if (in == "q") return
          if (in == "h") {
            players(i).highlight()
            game.update()
            if (in == "q") return
            in = readLine
          }
          if (in.length == 2) {
            x = mapInput(in(0))
            y = in(1).asDigit - 1
          }
      }
      if (players(i).set(x, y)) {
        i = if (i == 1) 0 else 1
        game.update()
      } else {
        println(f"Please try again. Possible moves for ${players(i)}:")
        suggestions(players(i)).foreach(move => print(f"$move "))
        println
      }
    }
    println(winner(players))
  }

  def winner(p: Vector[Player]): String = {
    val winner = if (p(0).count >= p(1).count) p(0) else p(1)
    val loser = if (winner == p(0)) p(1) else p(0)
    val winnerCount = winner.count
    val loserCount = loser.count
    if (winnerCount != loserCount) {
      return f"$winner wins by $winnerCount:$loserCount!"
    }
    f"Draw. $winnerCount:$loserCount"
  }

  def suggestions(p: Player): List[String] = {
    for { e <- p.moves.values.flatten.toSet.toList.sorted
          move = mapOutput(e._1) + (e._2 + 1)
    } yield move
  }

  def mapOutput(out: Int): String = (out + 65).toChar.toString

  def mapInput(in: Char): Int =  {
    val u = 'A' to 'H'
    val l = 'a' to 'h'
    if (u.contains(in) || l.contains(in)) in.toUpper.toInt - 65
    else 9
  }
}
