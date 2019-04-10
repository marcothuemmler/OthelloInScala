package de.htwg.se.othello

import de.htwg.se.othello.model.{Board, Game, Player}

import scala.io.StdIn
import scala.util.Random

object Othello {

  def main(args: Array[String]): Unit = {
    val game = Game(new Board)
    val p1 = new Player("Player1", 1, game)
    val p2 = new Player("Bot", 2, game)
    val player = Array(p1, p2)
    var input = ""
    var i = 0
    var x, y = -1
    while (player(0).moves.nonEmpty || player(1).moves.nonEmpty) {
      if (player(i).moves.isEmpty) {
        print(f"${player(i).toString} has no possible moves. ")
        i = if (i == 1) 0 else 1
        println(f"${player(i).toString}'s turn")
      }
      if (i == 0) {
        player(i).highlight()
        game.update()
        input = StdIn.readLine()
        if (input == "q") return
        val tokens = input.split(" ")
        if (tokens.length == 2) {
          x = tokens(0).toInt
          y = tokens(1).toInt
        }
      } else {
        val tile = getOrigin(player(i).moves)
        val move = tile._2(Random.nextInt(tile._2.size))
        x = move._1
        y = move._2
        Thread.sleep(500)
      }
      if (player(i).set(x, y)) {
        i = if (i == 1) 0 else 1
        game.update()
      } else {
        println(f"Please try again. Possible moves for ${player(i)}%s:")
        val possible = player(i).moves.values.flatten.toSet
        for (move <- possible) {
          print(f"$move ")
        }
        println
      }
    }
    val p1tiles = p1.count()
    val p2tiles = p2.count()
    if (p1tiles > p2tiles) {
      println(f"${p1.toString} won by $p1tiles:$p2tiles!")
    }else if (p1.count() == p2.count()) {
      println("Draw")
    } else {
      println(f"${p2.toString} won by $p2tiles:$p1tiles!")
    }
  }


  def getOrigin(map: Map[(Int, Int), List[(Int, Int)]]): ((Int, Int), List[(Int, Int)]) = {
    map.toList(Random.nextInt(map.keySet.size))
  }
}
