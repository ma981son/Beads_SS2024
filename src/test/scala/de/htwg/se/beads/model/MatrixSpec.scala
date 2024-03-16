package de.htwg.se.beads.model

import org.scalatest.wordspec.AnyWordSpec

import org.scalatest.matchers.should.Matchers
import _root_.de.htwg.se.beads.util.Enums.*

class MatrixSpec extends AnyWordSpec with Matchers {

  "A Matrix " when {

    val white = Color(255, 255, 255)
    val red = Color(255, 0, 0)

    val bead = Bead()

    val matrix =
      new Matrix(row = 1, col = 1)

    "created with row, column, a starting color and a stitch" should {

      "have the correct size" in {
        matrix.size should be(1, 1)
      }

      "have the correct stitch" in {
        matrix.stitch should be(Stitch.Square)
      }

      "contain beads with the correct starting color" in {
        matrix.allBeads.foreach(bead => bead.beadColor should be(white))
      }

      "throw an exception if the dimensions are invalid" in {
        an[IllegalArgumentException] should be thrownBy {
          new Matrix(-1, 0)
        }
        an[IllegalArgumentException] should be thrownBy {
          new Matrix(0, -1)
        }
        an[IllegalArgumentException] should be thrownBy {
          new Matrix(0, 0)
        }
      }
    }

    "created with a vector of vector of beads" should {

      val vectorMatrix = Matrix(
        Vector(Vector(bead)),
        stitch = Stitch.Square
      )

      "have the correct size" in {
        vectorMatrix.size should be(1, 1)
      }

      "have the correct stitch" in {
        matrix.stitch should be(Stitch.Square)
      }

      "contain beads with the correct starting color" in {
        matrix.allBeads.foreach(bead => bead.beadColor should be(white))
      }
    }

    "changing its size" should {

      "return a copy of the matrix in the correct size" in {
        val newMatrix = matrix.changeSize(4, 4)
        newMatrix.size should be(4, 4)
      }

      "throw an exception if the dimentions are invalid" in {
        an[IllegalArgumentException] should be thrownBy {
          matrix.changeSize(-1, 0)
        }
        an[IllegalArgumentException] should be thrownBy {
          matrix.changeSize(0, -1)
        }
        an[IllegalArgumentException] should be thrownBy {
          matrix.changeSize(0, 0)
        }
      }
    }

    "accessing a bead in a specific position" should {

      "return the bead" in {
        val newBead = matrix.bead(0, 0)
        newBead.beadCoord should be(Coord(0.0, 0.0))
        newBead.beadColor should be(white)
      }

      "throw an exception if the dimensions are out of bounds" in {
        an[java.lang.IndexOutOfBoundsException] should be thrownBy {
          matrix.bead(20, 20)
        }
      }
    }

    "accessing all beads" should {

      "return a List with all beads" in {
        List(
          Bead()
        )
      }
    }

    "replacing a bead in a specific position" should {

      "return a new matrix with the correct bead replaced" in {
        val newBead = Bead(beadColor = red)
        val newMatrix = matrix.replaceBead(0, 0, newBead)
        newMatrix.matrix(0)(0) should be(newBead)
        newMatrix.bead(0, 0).beadCoord should be(Coord(0, 0))
        newMatrix.bead(0, 0).beadColor should be(red)
      }
    }

    "changing the stitch of the matrix" should {

      "return a matrix with the correct stitch" in {
        val newMatrix = matrix.changeStitch(Stitch.Brick)
        newMatrix.stitch should be(Stitch.Brick)
        newMatrix.size should be(matrix.size)
        newMatrix.allBeads should be(matrix.allBeads)
      }
    }

    "filling with a new bead" should {

      "return a new Matrix with all the beads replaced" in {
        val newBead = Bead(beadColor = red)
        val newMatrix = matrix.fill(newBead)
        newMatrix.allBeads.foreach(bead => bead.beadColor should be(red))
        newMatrix.size should be(matrix.size)
      }
    }

  }
}
