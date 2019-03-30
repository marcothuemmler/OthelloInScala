package de.htwg.se.othello

case class Board() {

  val field: Array[Array[Cell]] = Array.tabulate(8, 8)((i, j) => {
    if ((i == 4 || i == 3) && i == j) Cell(1)
    else if (i == 4 && j == 3 || i == 3 && j == 4) Cell(2) else Cell(0)
  })

  override def toString: String = {
    val sb = new StringBuilder(" -----------------\n")
      for (j <- field.indices.indices) {
        sb ++="| "
        for (i <- field.indices) {
          sb ++= field(i)(j).toString + " "
        }
        sb ++= "|\n"
    }
    sb.append(" -----------------").toString
  }

  def flip(x: Int, y: Int, player: Player): Unit = {
    field(x)(y) = Cell(player.value)
  }
}