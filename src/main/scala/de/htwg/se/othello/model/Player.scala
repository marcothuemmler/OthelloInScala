package de.htwg.se.othello.model

class Player(name: String, value: Int, game: Game) {

  def this(value: Int, game: Game) = this(f"Player$value", value, game)

  def moves: Map[(Int, Int), List[(Int, Int)]] = {
    (for {i <- 0 to 7; j <- 0 to 7 if setByPl(i, j)
          move = getMoves(i, j)
    } yield move).filter(_._2.nonEmpty).toMap
  }

  def getMoves(x: Int, y: Int): ((Int, Int), List[(Int, Int)]) = {
    ((x, y), (for {i <- -1 to 1; j <- -1 to 1
                   tile = check(x, y, (i, j))
    } yield tile).filter(_ != (-1, -1)).toList)
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
      valid.keys.foreach(tile => game.flipLine((x, y), tile, value))
      allMoves.values.flatten.filter(_ != (x, y))
        .foreach(tile => game.flip(tile._1, tile._2, 0))
      true
    } else {
      false
    }
  }

  // Copy game
  /*def set(x: Int, y: Int): Game = {
    val valid = moves.filter(_._2.contains((x, y)))
    newGame = if (valid.nonEmpty) {
      (for { e <- valid.keys
        g = game.flipLine((x,y), e, value)
      } yield g).last
    } else {
      game
    }
    newGame
  }*/

  // Copy player
  /*def set(x: Int, y: Int): Player = {
    val valid = moves.filter(_._2.contains((x, y)))
    copy(game = {
      if (valid.nonEmpty) {
        (for {e <- valid.keys
              g = game.flipLine((x, y), e, value)
        } yield g).last
      }
      game
    })
  }*/

  def highlight(): Unit = {
    moves.values.flatten.foreach(e => game.flip(e._1, e._2, -1))
  }

  def setByPl(x: Int, y: Int): Boolean = game.valueOf(x, y) == value

  def setByOpp(x: Int, y: Int): Boolean = game.isSet(x, y) && !setByPl(x, y)

  def count: Int = game.field.flatten.count(_.value == value)

  override def toString: String = name
}
