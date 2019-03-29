object main {
  def main(args: Array[String]): Unit = {

    val game: Board = new Board
    val player1 = Player("Marco", 1)
    val player2 = Player("Yue", 2)
    val rules = Rules(game)
    println(game)
    println(f"$player1%s \n$player2%s")
    game.field(0)(0) = Cell(5)
  }
}
