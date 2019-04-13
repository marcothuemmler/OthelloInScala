package de.htwg.se.othello.model

case class Game(field: Array[Array[Cell]]) {

  def this() = {
    this(Array.tabulate(8, 8)((i, j) => {
      if ((i == 4 || i == 3) && i == j) Cell(2)
      else if (i == 4 && j == 3 || i == 3 && j == 4) Cell(1) else Cell(0)
    }))
  }

  override def toString: String = {
    val sb = new StringBuilder("\n    A B C D E F G H\n    _______________\n")
    for (i <- field.indices) {
      sb ++= (i + 1) + "  |"
      for (j <- field.indices)
        sb ++= field(j)(i).toString
      sb ++= "\n"
    }
    sb.append("    ⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺").toString
  }

  def flipLine(cur: (Int, Int), last: (Int, Int), value: Int): Unit = {
    val next = {
      (cur._1 - cur._1.compareTo(last._1), cur._2 - cur._2.compareTo(last._2))
    }
    flipCell(cur._1, cur._2, value)
    if (cur != last) {
      flipLine(next, last, value)
    }
  }

  def flipCell(x: Int, y: Int, newVal: Int): Unit = field(x)(y) = Cell(newVal)

  def update(): Unit = println(this)

  def valueOf(x: Int, y: Int): Int = field(x)(y).value
}
