package de.htwg.se.beads_util.Enums

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import io.AnsiColor._
import de.htwg.se.beads_util.Enums.ColorConverter
import scalafx.scene.paint.Color

class ColorsSpec extends AnyWordSpec with Matchers {

  "A Color Converter" when {

    "converting a String to Ansi" should {

      "return the correct Ansi color" in {
        val stringColor = "black"
        ColorConverter.stringToAnsi.get(stringColor) shouldEqual Some(BLACK_B)
      }

      "return None for invalid colors" in {
        val stringColor = "invalid_color"
        ColorConverter.stringToAnsi.get(stringColor) shouldEqual None
      }
    }

    "converting a Color to Ansi" should {

      "return the correct Ansi color" in {
        val color = Color.Black
        ColorConverter.rgbToAnsi.get(color) shouldEqual Some(BLACK_B)
      }

      "return None for invalid colors" in {
        val color = Color.AliceBlue
        ColorConverter.rgbToAnsi.get(color) shouldEqual None
      }
    }

    "converting a Color to a RGBA string" should {

      "return the correct RGBA string" in {
        val color = Color.rgb(255, 0, 0, 0.5)
        ColorConverter
          .colorToRGBA(color) shouldEqual "rgba(255, 000, 000, 0.5)"
      }
    }

    "converting an RGBA string to a Color" should {

      "return the correct Color" in {
        val rgbaString = "rgba(255, 0, 0, 0.5)"
        val color = Color.rgb(255, 0, 0, 0.5)
        ColorConverter.rgbaToColor(rgbaString) shouldEqual color
      }

      "throw an IllegalArgumentException for invalid RGBA format" in {
        val rgbaString = "invalid_format"
        an[IllegalArgumentException] should be thrownBy ColorConverter
          .rgbaToColor(rgbaString)
      }
    }
  }
}
