package de.htwg.se.othello.model

case class Board(grid: Vector[Vector[Square]]) {

  def this() = this(Vector.tabulate(8, 8)((i, j) => {
    if ((i == 4 || i == 3) && i == j) Square(2)
    else if (i == 4 && j == 3 || i == 3 && j == 4) Square(1)
    else Square(0)
  }))

  def moves(value: Int): Map[(Int, Int), Stream[(Int, Int)]] = {
    (for {
      col <- 0 to 7
      row <- 0 to 7 if setBy(value, col, row)
    } yield getMoves(value, col, row)).filter(o => o._2.nonEmpty).toMap
  }

  def getMoves(value: Int, col: Int, row: Int): ((Int, Int), Stream[(Int, Int)]) = {
    ((col, row), (for {
      x <- -1 to 1
      y <- -1 to 1
      (nX, nY) = (col + x, row + y)
      if (0 to 7 contains nX) && (0 to 7 contains nY) && setByOpp(value, nX, nY)
    } yield checkRec(value, nX, nY, (x, y))).filter(o => o != (-1, -1)).toStream)
  }

  def corners(value: Int): Int = {
    (if (setBy(value, 0 ,0)) 3 else 0) +
      (if (setBy(value, 0 ,7)) 3 else 0) +
      (if (setBy(value, 7 ,0)) 3 else 0) +
      (if (setBy(value, 7 ,7)) 3 else 0)
  }

  def checkRec(value: Int, x: Int, y: Int, direction: (Int, Int)): (Int, Int) = {
    val (nX, nY) = (x + direction._1, y + direction._2)
    if (nX < 0 || nX > 7 || nY < 0 || nY > 7 || setBy(value, nX, nY)) (-1, -1)
    else if (setByOpp(value, nX, nY)) checkRec(value, nX, nY, direction)
    else (nX, nY)
  }

  def flip(col: Int, row: Int, value: Int): Board = {
    copy(grid.updated(col, grid(col).updated(row, Square(value))))
  }

  def flipLine(current: (Int, Int), end: (Int, Int), value: Int): Board = {
    val board = flip(current._1, current._2, value)
    val nextH = current._1 - current._1.compare(end._1)
    val nextV = current._2 - current._2.compare(end._2)
    if (current != end) board.flipLine((nextH, nextV), end, value)
    else board
  }

  def deHighlight: Board = copy(Vector.tabulate(8, 8)((col, row) => {
    if (grid(col)(row).isHighlighted) Square(0) else grid(col)(row)
  }))

  def highlight(value: Int): Board = copy(Vector.tabulate(8, 8)((col, row) => {
    if (moves(value).values.flatten.toSet.contains((col, row))) Square(-1)
    else grid(col)(row)
  }))

  def isSet(col: Int, row: Int): Boolean = grid(col)(row).isSet

  def setBy(value: Int, x: Int, y: Int): Boolean = valueOf(x, y) == value

  def setByOpp(value: Int, x: Int, y: Int): Boolean = isSet(x, y) && !setBy(value, x, y)

  def valueOf(col: Int, row: Int): Int = grid(col)(row).value

  def isHighlighted: Boolean = grid.flatten.count(o => o.isHighlighted) > 0

  def count: (Int, Int) = (count(1), count(2))

  def count(value: Int): Int = grid.flatten.count(o => o.value == value)

  def gameOver: Boolean = moves(1).isEmpty && moves(2).isEmpty

  def score: String = {
    val (win, lose) = (count._1 max count._2, count._1 min count._2)
    val winner = if (win == count._1) "Black" else "White"
    if (win != lose) f"$winner wins by $win:$lose!" else f"Draw. $win:$lose"
  }

  override def toString: String = {
    val top = "\n    A B C D E F G H\n    _______________"
    var board = ("\nrow  |" + ("X" * 8)) * 8 + "\n"
    for {
      col <- 0 to 7
      row <- 0 to 7
    } board = board.replaceFirst("row", f"${row + 1}")
      .replaceFirst("X", f"${grid(row)(col)}")
    top + board + "    ⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺" + (if (gameOver) "\n" + score else "")
  }
}
