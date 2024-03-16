package de.htwg.se.beads.model

import org.scalatest.wordspec.AnyWordSpec

import org.scalatest.matchers.should.Matchers
import Enums.DefaultColors

class BeadSpec extends AnyWordSpec with Matchers {

  "A Bead" when {

    val white = Color(255, 255, 255)
    val red = Color(255, 0, 0)
    val bead = Bead()

    "created" should {

      "have coordinates" in {
        bead.beadCoord should be(Coord(0.0, 0.0))
      }
      "have the default beadcolor" in {
        bead.beadColor should be(DefaultColors.NoColor.color)
      }
    }

    "not colored" should {

      "should return false for isColored" in {
        bead.isColored should be(false)
      }
    }

    "colored" should {

      val bead = Bead(beadColor = red)

      "should return true on isColored" in {
        bead.isColored should be(true)
      }
    }

    "changed in color" should {

      val coloredBead = bead.changeColor(red)

      "have the new color" in {
        coloredBead.beadColor should be(red)
      }

      "have the same coordinates" in {
        coloredBead.beadCoord should be(bead.beadCoord)
      }
    }

    "compared to another Bead with the same attributes" should {

      val bead2 = Bead()

      "be equal" in {
        bead.equals(bead2) shouldBe true
      }
    }

    "compared to another Bead with different attributes" should {

      val bead2 = Bead(Coord(1, 2), red)

      "not be equal" in {
        bead.equals(bead2) shouldBe false
      }
    }

    "compared to something different" should {

      val compare = List(1, 2, 3)

      "not be equal" in {
        bead.equals(compare) should be(false)
      }
    }

    "converted to a string" should {

      val expectedString = "| Color(255.0,255.0,255.0) |"

      "have a string representation with the correct format" in {
        bead.toString shouldBe expectedString
      }
    }

  }
}
