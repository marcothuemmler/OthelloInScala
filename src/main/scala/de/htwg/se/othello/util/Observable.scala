package de.htwg.se.othello.util

trait Observer {
  def update(): Unit
}

class Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers.filterNot(o => o == s)

  def notifyObservers(): Unit = subscribers.foreach(o => o.update())
}
