package de.htwg.se.othello.model

import scala.io.StdIn.readLine

class Controller() {

  def playGame(): Unit = {
    val board = new Board
    val players = Vector(new Player(1, board), new Bot(2, board))
    var x, y = -1
    var i = 0
    var input = ""
    println(board)
    while (!gameOver(players) && input != "q") {
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
          input = readLine
            if (input == "h") {
              println(board)
              input = readLine
            }
          if (input.length == 2) {
            x = mapToBoard(input)._1
            y = mapToBoard(input)._2
          }
      }
      if (players(i).set(x, y)) {
        i = if (i == 1) 0 else 1
        println(board)
      } else if (input != "q") {
        println(f"Please try again. Possible moves for ${players(i)}:")
        suggestions(players(i)).foreach(move => print(f"$move "))
        println
      }
    }
    if (gameOver(players)) {
      println(f"${result(players)}\n")
      if (readLine("Press \"y\" for new game\n") == "y") playGame()
    }
  }

  def result(p: Vector[Player]): String = {
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

  def gameOver(players:Vector[Player]): Boolean = {
    players(0).moves.isEmpty && players(1).moves.isEmpty
  }

  def suggestions(p: Player): List[String] = {
    for {e <- p.moves.values.flatten.toSet.toList.sorted
    } yield mapOutput(e._1, e._2)
  }

  def mapOutput(x: Int, y: Int): String = (x + 65).toChar.toString + (y + 1)

  def mapToBoard(input: String): (Int, Int) = {
    (input(0).toUpper.toInt - 65, input(1).asDigit - 1)
  }
}
