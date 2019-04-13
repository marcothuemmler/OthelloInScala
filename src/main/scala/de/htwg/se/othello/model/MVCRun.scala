package de.htwg.se.othello.model

import scala.io.StdIn

case class MVCRun() {

  val game = new Game

  def playGame(players: Vector[Player]): Unit = {
    var x, y = -1
    var i = 0
    var input = ""
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
          println(f"${players(i)} is setting ${mapOut(x)}${y + 1}")
        case _ =>
          input = StdIn.readLine
          if (input == "q") return
          if (input == "h") {
            players(i).highlight()
            game.update()
            input = StdIn.readLine()
            if (input == "q") return
          }
          if (input.length == 2) {
            x = mapIn(input(0))
            y = input(1).asDigit - 1
          }
      }
      if (players(i).set(x, y)) {
        i = if (i == 1) 0 else 1
        game.update()
      } else {
        println(f"Please try again. Possible moves for ${players(i)}:")
        suggestions(players(i)).foreach(move => print(s"$move "))
      }
    }
    println(winner(players))
  }

  def winner(player: Vector[Player]): String = {
    val winner = if (player(0).count > player(1).count) player(0) else player(1)
    val loser = if (winner == player(0)) player(1) else player(0)
    val winnerCount = winner.count
    val loserCount = loser.count
    if (winnerCount != loserCount) {
      s"$winner wins by $winnerCount:$loserCount!"
    } else {
      s"Draw. $winnerCount:$loserCount"
    }
  }

  def suggestions(p: Player): List[String] = {
    for { e <- p.moves.values.flatten.toSet.toList.sorted
          move = mapOut(e._1) + (e._2 + 1)
    } yield move
  }

  def player(kind: String, value: Int): Player = {
    if (kind == "bot") new Bot(value, game) else new Player(value, game)
  }

  def mapOut(out: Int): String = out match {
    case 0 => "A"
    case 1 => "B"
    case 2 => "C"
    case 3 => "D"
    case 4 => "E"
    case 5 => "F"
    case 6 => "G"
    case 7 => "H"
    case _ => "_"
  }

  def mapIn(in: Char): Int = in match {
    case 'a' | 'A' => 0
    case 'b' | 'B' => 1
    case 'c' | 'C' => 2
    case 'd' | 'D' => 3
    case 'e' | 'E' => 4
    case 'f' | 'F' => 5
    case 'g' | 'G' => 6
    case 'h' | 'H' => 7
    case _ => 9
  }
}
