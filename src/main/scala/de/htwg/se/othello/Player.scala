case class Player(name: String, value: Int) {

  val tileColor: String = if (value == 1) "white" else "black"

  override def toString: String = {
    f"$name%s plays with $tileColor%s tiles"
  }
}