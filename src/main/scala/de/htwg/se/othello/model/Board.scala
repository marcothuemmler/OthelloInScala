package de.htwg.se.othello.model

class Board {

  val field: Array[Array[Cell]] = Array.tabulate(8, 8)((i, j) => {
    if ((i == 4 || i == 3) && i == j) Cell(2)
    else if (i == 4 && j == 3 || i == 3 && j == 4) Cell(1) else Cell(0)
  })

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
}
