package de.htwg.se.beads.model.gridComponent.gridBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CoordSpec extends AnyWordSpec with Matchers {

  "A Coord" when {

    def coord = Coord(1, 1)

    "created" should {

      "have the correct x coordinate" in {
        coord.x should be(1)
      }
      "have the correct y coordinate" in {
        coord.y should be(1)
      }
    }

    "converted to a string" should {

      val expectedString = "1.0,1.0"

      "have a string representation with the correct format" in {
        coord.toString shouldBe expectedString
      }
    }
  }
}
