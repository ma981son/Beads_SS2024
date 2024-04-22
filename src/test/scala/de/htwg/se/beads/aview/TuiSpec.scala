package de.htwg.se.beads.aview

import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.aview.Tui
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads.controller.Controller
import scalafx.scene.paint.Color

class TuiSpec extends AnyWordSpec with Matchers {

  "A TUI" when {

    val grid = new Grid(1, 1)

    "processing an input size line" should {

      "on an valid input create a new grid" in {
        val controller = new Controller(grid)
        val tui = new Tui(controller)
        val input = "5 5 square"
        val expectedGrid = new Grid(5, 5)
        tui.processInputSizeLine(input)
        controller.grid should be(expectedGrid)
      }

      "don't change the grid on an invalid input" in {
        val controller = new Controller(grid)
        val tui = new Tui(controller)
        val input = "pelusa"
        tui.processInputSizeLine(input)
        controller.grid should be(grid)
      }
    }

    "processing an input line" should {

      "change the grid size when given command size and a valid input" in {
        val controller = new Controller(grid)
        val tui = new Tui(controller)
        val input = "size 3 3"
        val expectedGrid = new Grid(3, 3)
        tui.processInputLine(input)
        controller.grid should be(expectedGrid)
      }

      "change a beads color when given a valid input" in {
        val controller = new Controller(grid)
        val tui = new Tui(controller)
        val input = "0 0 red"
        val expectedGrid = grid.setBeadColor(0, 0, Color(255, 0, 0, 1))
        tui.processInputLine(input)
        controller.grid should be(expectedGrid)
      }

      "create a new grid on command n" in {}

      "don't change the grid on an invalid input" in {
        val controller = new Controller(grid)
        val tui = new Tui(controller)
        val input = "pelusa"
        tui.processInputLine(input)
        controller.grid should be(grid)
      }
    }
  }
}
