package de.htwg.se.othello.controller

object GameStatus extends  Enumeration{
  type GameStatus = Value
  val IDLE, NEWGAME, FINISHED = Value

  val map: Map[GameStatus, String] = Map[GameStatus, String](
    IDLE -> "",
    NEWGAME -> "A new game was created.",
    FINISHED -> "Finished"




  )

}
