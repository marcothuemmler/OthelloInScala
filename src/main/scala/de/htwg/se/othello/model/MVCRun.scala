package de.htwg.se.othello.model

import scala.io.StdIn.readLine

class MVCRun() {

  def playGame(): Unit = {
    val game = new Game
    val players = Vector(new Player(1, game), new Bot(2, game))
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
          Thread.sleep(500)
          println(f"$bot sets ${mapOutput(x, y)}")
        case _ =>
          in = readLine
          if (in == "q") return
          if (in == "h") {
            players(i).highlight()
            game.update()
            in = readLine
            if (in == "q") return
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
    println(f"${winner(players)}\n")
    Thread.sleep(500)
    println("Press \"y\" for new game")
    if (readLine == "y") playGame()
  }

  def winner(p: Vector[Player]): String = {
    val p1count = p(0).count
    val p2count = p(1).count
    val winner = if (p1count >= p2count) p(0) else p(1)
    val loser = if (winner == p(0)) p(1) else p(0)
    if (p1count != p2count) {
      f"$winner wins by ${winner.count}:${loser.count}!"
    } else {
      f"Draw. $p1count:$p2count"
    }
  }

  def suggestions(p: Player): List[String] = {
    for {e <- p.moves.values.flatten.toSet.toList.sorted
         move = mapOutput(e._1, e._2)
    } yield move
  }

  def mapOutput(x: Int, y: Int): String = (x + 65).toChar.toString + (y + 1)

  def mapInput(in: Char): Int = in.toUpper.toInt - 65
}
