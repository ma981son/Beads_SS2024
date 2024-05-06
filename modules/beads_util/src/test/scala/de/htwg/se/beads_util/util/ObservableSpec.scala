package de.htwg.se.beads_util.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.util.Observable

class ObservableSpec extends AnyWordSpec with Matchers {

  "An Observer" when {

    "notifying observers" should {

      "return true after updating" in {
        val observer = new Observer {
          override def update(): Boolean = true
        }
        observer.update() shouldBe true
      }
    }
  }

  "An Observable" when {

    "adding observers" should {

      "have the observer in the list of subscribers" in {
        val observable = new Observable
        val observer = new Observer {
          override def update(): Boolean = true
        }
        observable.add(observer)
        observable.subscribers should contain(observer)
      }
    }

    "removing observers" should {

      "not have the observer in the list of subscribers" in {
        val observable = new Observable
        val observer = new Observer {
          override def update(): Boolean = true
        }
        observable.add(observer)
        observable.remove(observer)
        observable.subscribers should not contain observer
      }
    }

    "notifying observers" should {

      "call update method of all subscribers" in {
        var updatedCount = 0
        val observer1 = new Observer {
          override def update(): Boolean = {
            updatedCount += 1
            true
          }
        }
        val observer2 = new Observer {
          override def update(): Boolean = {
            updatedCount += 1
            true
          }
        }
        val observable = new Observable
        observable.add(observer1)
        observable.add(observer2)
        observable.notifyObservers()
        updatedCount shouldBe 2
      }
    }
  }

}
