package de.htwg.se.othello.model

import play.api.libs.json.{JsObject, JsValue, Json}

case class Player(name: String, value: Int) {

  def this(value: Int) = this(if (value == 1) "Black" else "White", value)

  def isBot: Boolean = isInstanceOf[Bot]

  override def toString: String = this.name

  def toJson: JsObject = {
    Json.obj("name" -> name, "value" -> value, "isBot" -> isBot)
  }
}

object Player {
  def fromJson(playerJson: JsValue): Player = {
    val name = (playerJson \ "name").as[String]
    val value = (playerJson \ "value").as[Int]
    val isBot = (playerJson \ "isBot").as[Boolean]
    if (isBot) new Bot(name, value) else Player(name, value)
  }
}
