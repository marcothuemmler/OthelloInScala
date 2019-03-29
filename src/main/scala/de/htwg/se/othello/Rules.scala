case class Rules(board: Board) {

  def checkSet(x: Int, y: Int): Boolean = board.field(x)(y).isSet

  def getColor(player: Player, x: Int, y: Int): Boolean = {
    checkSet(x, y) && board.field(x)(y).value == player.value
  }
}
