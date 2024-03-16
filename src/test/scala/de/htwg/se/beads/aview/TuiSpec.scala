package de.htwg.se.beads.model.aview

import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.Color
import de.htwg.se.beads.model.aview.Tui
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TuiSpec extends AnyWordSpec with Matchers {

  "A TUI" when {

    val grid = new Grid(1, 1)
    val tui = new Tui

    "processing an input size line" should {

      "on an valid input create a new grid" in {
        val input = "5 5"
        val expectedGrid = new Grid(5, 5)
        tui.processInputSizeLine(input, grid) should be(expectedGrid)
      }

      "on an invalid input show the old grid" in {
        val input = "pelusa"
        tui.processInputSizeLine(input, grid) should be(grid)
      }
    }

    "processing an input line" should {

      "change the grid size when given command size and a valid input" in {
        val input = "size 3 3"
        val expectedGrid = new Grid(3, 3)
        tui.processInputLine(input, grid) should be(expectedGrid)
      }

      "change a beads color when given a valid input" in {
        val input = "0 0 red"
        val expectedGrid = grid.setBeadColor(0, 0, Color(255, 0, 0))
        tui.processInputLine(input, grid) should be(expectedGrid)
      }

      "create a new grid on command n" in {}

      "don't change the grid on an invalid input" in {
        val input = "pelusa"
        tui.processInputLine(input, grid) should be(grid)
      }
    }

  }
}
