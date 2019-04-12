package de.htwg.se.othello.model

import scala.io.StdIn

case class MVCRun() {

  val game: Game = Game(new Board)

  def playGame(players: Vector[Player]): Unit = {
    var x, y = -1
    var i = 0
    var input = ""
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
          //players(i).highlight()
          game.update()
          input = StdIn.readLine
          if (input == "q") return
          if (input == "h") {
            players(i).highlight()
            game.update()
            input = StdIn.readLine()
          }
          if (input.length == 2) {
            x = matchInput(input(0))
            y = input(1).asDigit - 1
          }
      }
      if (!players(i).set(x, y)) {
        println(f"Please try again. Possible moves for ${players(i)}:")
        val possible = players(i).moves.values.flatten.toSet.toList
        val sorted = possible.sorted
        sorted.foreach(e => print(f"${matchInput(e._1)}${e._2 + 1} "))
        println
      } else {
        i = if (i == 1) 0 else 1
        game.update()
      }
    }
    println(winner(players))
  }

  def winner(p: Vector[Player]): String = {
    val winner = if (p(0).count > p(1).count) p(0) else p(1)
    val loser = if (winner == p(0)) p(1) else p(0)
    if (p(0).count != p(1).count) {
      s"$winner wins by ${winner.count}:${loser.count}!"
    } else {
      s"Draw. ${p(0).count}:${p(1).count}"
    }
  }

  def matchInput(index: Int): Char = index match {
    case 0 => 'A'
    case 1 => 'B'
    case 2 => 'C'
    case 3 => 'D'
    case 4 => 'E'
    case 5 => 'F'
    case 6 => 'G'
    case 7 => 'H'
    case _ => 'x'
  }

  def matchInput(index: Char): Int = index match {
    case 'a' => 0
    case 'A' => 0
    case 'b' => 1
    case 'B' => 1
    case 'c' => 2
    case 'C' => 2
    case 'd' => 3
    case 'D' => 3
    case 'e' => 4
    case 'E' => 4
    case 'f' => 5
    case 'F' => 5
    case 'g' => 6
    case 'G' => 6
    case 'h' => 7
    case 'H' => 7
    case _ => 9
  }

  def player(kind: String, value: Int): Player = {
    if (kind == "bot") new Bot(value, game) else new Player(value, game)
  }
  def player(value: Int): Player = new Player(value, game)
}
