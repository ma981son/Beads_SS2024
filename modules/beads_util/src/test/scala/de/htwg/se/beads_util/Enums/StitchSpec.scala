package de.htwg.se.beads_util.Enums

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads_util.Enums.{StitchConverter, Stitch}

class StitchSpec extends AnyWordSpec with Matchers {

  "A Stitch Converter" when {

    "converting a String to Stitch" should {

      "return the correct Stitch" in {
        val stringStitch = "brick"
        StitchConverter.stringToStitch.get(stringStitch) shouldEqual Some(
          Stitch.Brick
        )
      }

      "return None for invalid stitch strings" in {
        val stringStitch = "invalid_stitch"
        StitchConverter.stringToStitch.get(stringStitch) shouldEqual None
      }
    }
  }
}
