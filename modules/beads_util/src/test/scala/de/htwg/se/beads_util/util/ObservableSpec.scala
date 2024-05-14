package de.htwg.se.beads_util.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.util.Observable
import de.htwg.se.beads_util.Enums.Event

class ObservableSpec extends AnyWordSpec with Matchers {

  "An Observer" when {

    "notifying observers" should {}
  }

  "An Observable" when {

    "adding observers" should {}

    "removing observers" should {

      "not have the observer in the list of subscribers" in {}
    }

    "notifying observers" should {

      "call update method of all subscribers" in {}
    }
  }

}
