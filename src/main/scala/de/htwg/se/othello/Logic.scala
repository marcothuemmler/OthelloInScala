package de.htwg.se.othello
// TODO: Flesh out checks, implement diagonal, south, left, check edge cases
case class Logic(board: Board) {

  def setByPlayer(x: Int, y: Int, player: Player): Boolean = {
    board.getValue(x,y) == player.value
  }

  def checkNorth(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x, y - 1)) {
      checkRecNorth(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkRight(x: Int, y: Int, player: Player): (Int, Int) = {
    if (setByPlayer(x, y, player) && board.isSet(x + 1, y)) {
      checkRecRight(x, y, player)
    } else {
      (-1, -1)
    }
  }

  def checkRecNorth(x: Int, y: Int, player: Player): (Int, Int) = {
    val yVal = y - 1
    if (board.isSet(x, yVal) && board.getValue(x, yVal) != player.value){
      checkRecNorth(x, yVal, player)
    } else if (board.getValue(x, yVal) == player.value) {
      (-1,-1)
    } else {
      (x, yVal)
    }
  }

  def checkRecRight(x: Int, y: Int, player: Player): (Int, Int) = {
    val xVal = x + 1
    if (board.isSet(xVal, y) && board.getValue(xVal, y) != player.value){
      checkRecRight(xVal, y, player)
    } else if (board.getValue(xVal, y) == player.value) {
      (-1,-1)
    } else {
      (xVal, y)
    }
  }
}
