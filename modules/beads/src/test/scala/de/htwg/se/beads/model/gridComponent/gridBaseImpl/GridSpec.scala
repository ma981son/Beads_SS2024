package de.htwg.se.beads.model.gridComponent.gridBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads_util.Enums.*
import scala.io.AnsiColor._
import scalafx.scene.paint.Color
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Bead
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Matrix
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.BeadVector
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Coord

class GridSpec extends AnyWordSpec with Matchers {

  val white = Color(1.0, 1.0, 1.0, 1.0)
  val red = Color(1.0, 0.0, 0.0, 1.0)

  val bead = Bead()

  val grid = new Grid(2, 3)

  "A Grid" when {

    "created with a length, width a starting color and a stitch" should {

      "have the correct number of rows and columns" in {
        grid.length should be(2)
        grid.width should be(3)
      }

      "have the correct stitch" in {
        grid.stitch should be(Stitch.Square)
      }

      "have all its beads initialized with the starting color" in {
        grid
          .allBeads()
          .foreach(bead =>
            bead.beadColor should be(DefaultColors.NoColor.color)
          )
      }
    }
  }

  "created with a matrix of beads" should {

    val matrix = new Matrix(2, 3, red, Stitch.Brick)
    val grid = Grid(matrix)

    "have the correct number of rows and columns" in {
      grid.length should be(2)
      grid.width should be(3)
    }

    "have the correct stitch" in {
      grid.stitch should be(Stitch.Brick)
    }

    "have all its beads initialized with the startBeads color" in {
      grid
        .allBeads()
        .foreach(bead => bead.beadColor should be(bead.beadColor))
    }
  }

  "changing its size" should {

    "return a grid with the correct size" in {
      val newGrid = grid.changeSize(3, 4)
      newGrid.length should be(3)
      newGrid.width should be(4)
    }
  }

  "changing its stitch" should {

    "return a grid with the correct stitch" in {
      val newGrid = grid.changeStitch(Stitch.Brick)
      newGrid.stitch should be(Stitch.Brick)
      newGrid.length should be(grid.length)
      newGrid.width should be(grid.width)
      newGrid.allBeads() should be(grid.allBeads())
    }
  }

  "accessing a row" should {

    "return the correct BeadVector row" in {
      val expectedBeadVector = BeadVector(
        Vector(Bead(Coord(0, 0)), Bead(Coord(0, 1)), Bead(Coord(0, 2)))
      )
      grid.row(0) should be(expectedBeadVector)
    }
  }

  "accessing a column" should {

    "return the correct BeadVector col" in {
      val expectedBeadVector = BeadVector(
        Vector(Bead(Coord(0, 0)), Bead(Coord(1, 0)))
      )
      grid.col(0) should be(expectedBeadVector)
    }
  }

  "accessing all beads" should {

    "return a List with all beads" in {
      List(
        Bead(),
        Bead(),
        Bead(),
        Bead(),
        Bead(),
        Bead()
      )
    }
  }

  "accessing a bead in a specific position" should {

    "return the bead" in {
      val newBead = grid.bead(0, 0)
      newBead.beadCoord should be(Coord(0.0, 0.0))
      newBead.beadColor should be(DefaultColors.NoColor.color)
    }

    "throw an exception if the dimensions are out of bounds" in {
      an[java.lang.IndexOutOfBoundsException] should be thrownBy {
        grid.bead(20, 20)
      }
    }
  }

  "accessing a specific row of beads" should {

    val bead1 = Bead(Coord(0, 1))
    val bead2 = Bead(Coord(0, 2))
    val beadVector = Vector(bead, bead1, bead2)

    "return a vector of beads" in {
      grid.row(0) should be(BeadVector(beadVector))
    }
  }

  "accessing a specific column of beads" should {

    val bead1 = Bead(Coord(1, 0))
    val beadVector = Vector(bead, bead1)

    "return a vector of beads" in {
      grid.col(0) should be(BeadVector(beadVector))
    }
  }

  "setting the color of a bead in a specific position" should {

    "return a new grid with the new color set" in {
      val newGrid = grid.setBeadColor(0, 0, red)
      newGrid.bead(0, 0).beadColor should be(red)
    }

    "throw an exception if the dimensions are out of bounds" in {
      an[java.lang.IndexOutOfBoundsException] should be thrownBy {
        grid.setBeadColor(20, 20, red)
      }
    }
  }

  "converted to a string" should {

    val updatedGrid = grid
      .fillGrid(white)
    val expectedString = "\n" +
      "------------------" + "\n" +
      "|   |" + "|   |" + "|   |" + "\n" +
      "------------------" + "\n" +
      "|   |" + "|   |" + "|   |" + "\n" +
      "------------------"
      + "\n"

    "have a string representation with the correct format" in {
      updatedGrid.toString.replaceAll(
        "\u001B\\[[;\\d]*m",
        ""
      ) shouldBe expectedString
    }
  }

}
