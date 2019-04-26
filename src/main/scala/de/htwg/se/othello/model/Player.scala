package de.htwg.se.othello.model

class Player(name: String, value: Int, board: Board) {

  def this(value: Int, board: Board) = this(f"Player$value", value, board)

  def moves: Map[(Int, Int), Seq[(Int, Int)]] = {
    (for {
      i <- 0 to 7
      j <- 0 to 7 if setByPl(i, j)
    } yield getMoves(i, j)).filter(_._2.nonEmpty).toMap
  }

  def getMoves(x: Int, y: Int): ((Int, Int), Seq[(Int, Int)]) = {
    ((x, y), (for {
      i <- -1 to 1
      j <- -1 to 1 if !(i == 0 && j == 0)
    } yield check(x, y, (i, j))).filter(_ != (-1, -1)))
  }

  def check(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX > -1 && nX < 8 && nY > -1 && nY < 8 && setByOpp(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (-1, -1)
    }
  }

  def checkRec(x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val nX = x + direction._1
    val nY = y + direction._2
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setByPl(nX, nY)) {
      (-1, -1)
    } else if (setByOpp(nX, nY)) {
      checkRec(nX, nY, direction)
    } else {
      (nX, nY)
    }
  }

  def set(x: Int, y: Int): Boolean = {
    val allMoves = moves
    val valid = allMoves.filter(_._2.contains((x, y)))
    if (valid.nonEmpty) {
      valid.keys.foreach(board.flipLine((x, y), _, this.value))
      allMoves.values.flatten.filter(_ != (x, y)).foreach(board.flip(_, 0))
      true
    } else {
      false
    }
  }

  def highlight(): Unit = moves.values.flatten.foreach(board.flip(_, -1))

  def setByPl(x: Int, y: Int): Boolean = board.grid(x)(y).value == this.value

  def setByOpp(x: Int, y: Int): Boolean = {
    board.grid(x)(y).value > 0 && board.grid(x)(y).value != this.value
  }

  def count: Int = board.grid.flatten.count(_.value == this.value)

  override def toString: String = this.name
}
