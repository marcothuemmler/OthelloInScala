package de.htwg.se.othello.model

case class Game(field: Array[Array[Cell]]) {

  def this() = {
    this(Array.tabulate(8, 8)((i, j) => {
      if ((i == 4 || i == 3) && i == j) Cell(2)
      else if (i == 4 && j == 3 || i == 3 && j == 4) Cell(1)
      else Cell(0)
    }))
  }

  /*
  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Game = {
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    val newGame = flip(current._1, current._2, value)
    if (current != end) {
      newGame.flipLine((nextH, nextV), end, value)
    } else {
      newGame
    }
  }

  def flip(x: Int, y: Int, newVal: Int): Game = {
    copy(field.updated(x, field(x).updated(y, Cell(newVal))))
  }
  */

  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Unit = {
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    flip(current._1, current._2, value)
    if (current != end) {
      flipLine((nextH, nextV), end, value)
    }
  }

  def flip(x: Int, y: Int, newVal: Int): Unit = field(x)(y) = Cell(newVal)

  def update(): Unit = println(stringRep)

  def stringRep: String = {
    val sb = new StringBuilder("\n    A B C D E F G H\n    _______________\n")
    for (i <- 0 to 7) {
      sb ++= (i + 1) + "  |"
      for (j <- 0 to 7)
        sb ++= field(j)(i).toString
      sb ++= "\n"
    }
    sb.append("    ⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺").toString
  }

  def isSet(x: Int, y: Int): Boolean = field(x)(y).value > 0

  def valueOf(x: Int, y: Int): Int = field(x)(y).value
}
