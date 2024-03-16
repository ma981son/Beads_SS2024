package de.htwg.se.beads.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ColorSpec extends AnyWordSpec with Matchers {

  "A Color" when {

    def color = Color(255, 255, 255)

    "created with valid RGB values" should {

      "have the correct red value" in {
        color.r should be(255)
      }

      "have the correct green value" in {
        color.g should be(255)
      }

      "have the correct blue value" in {
        color.b should be(255)
      }
    }

    "created with invalid RGB values" should {

      "throw an IllegalArgumentException for a negative red value" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(-1, 255, 255)
        }
      }

      "throw an IllegalArgumentException for a red value greater than 255" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(256, 255, 255)
        }
      }

      "throw an IllegalArgumentException for a negative green value" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(255, -1, 255)
        }
      }

      "throw an IllegalArgumentException for a green value greater than 255" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(255, 256, 255)
        }
      }

      "throw an IllegalArgumentException for a negative blue value" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(255, 255, -1)
        }
      }

      "throw an IllegalArgumentException for a blue value greater than 255" in {
        an[IllegalArgumentException] should be thrownBy {
          Color(255, 255, 256)
        }
      }
    }

    "converted to a string" should {

      val expectedString = "Color(255.0,255.0,255.0)"

      "have a string representation with the correct format" in {
        color.toString() should be(expectedString)
      }
    }

  }
}
