package de.htwg.se.othello.util

import org.scalatest.{Matchers, WordSpec}

class ObservableSpec extends WordSpec with Matchers {
  "An Observable" should {
    val observable = new Observable
    val observer: Observer = new Observer {
      var updated: Boolean = false
      override def update: Boolean = true
    }
    "add an Observer" in {
      observable.add(observer)
      observable.subscribers should contain (observer)
    }
    "notify an Observer" in {
      observable.notifyObservers()
    }
    "remove an Observer" in {
      observable.remove(observer)
      observable.subscribers should not contain observer
    }

  }

}