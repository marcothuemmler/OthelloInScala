package de.htwg.se.othello.controller

object GameStatus extends  Enumeration{
  type GameStatus = Value
  val IDLE, NEWGAME, FINISHED, Set_Player1, Set_Bot = Value

  val map: Map[GameStatus, String] = Map[GameStatus, String](
    IDLE -> "",
    NEWGAME -> "A new game was created.",
    FINISHED -> "Finished",
    Set_Player1 -> "Cell set by Player1",
    Set_Bot -> "Cell set by Bot"

    //give a suggestion


  )

  def information(gamestatus: GameStatus) : String = map(gamestatus)

}
