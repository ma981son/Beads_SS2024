package de.htwg.se.beads.model.gridComponent.gridBaseImpl

import de.htwg.se.beads_util.Enums.{Stitch, DefaultColors}
import scala.collection.immutable.Vector
import scalafx.scene.paint.Color
import com.google.inject.Inject

case class Matrix @Inject() (
    matrix: Vector[Vector[Bead]],
    stitch: Stitch
) {

  def this(
      row: Int = 0,
      col: Int = 0,
      startColor: Color = DefaultColors.NoColor.color,
      stitch: Stitch = Stitch.Square
  ) =
    this(
      if (row > 0 && col > 0) {
        Vector.tabulate(row, col) { (x, y) =>
          Bead(Coord(x, y), startColor)
        }
      } else {
        throw new IllegalArgumentException("Invalid matrix size")
      },
      stitch
    )

  def changeSize(row: Int, col: Int): Matrix = {
    if (row > 0 && col > 0) {
      val newMatrix = Vector.tabulate(row, col) { (x, y) =>
        matrix
          .lift(x)
          .flatMap(_.lift(y))
          .getOrElse(Bead(Coord(x, y), DefaultColors.NoColor.color))
      }

      copy(matrix = newMatrix)
    } else {
      throw new IllegalArgumentException("Invalid matrix size")
    }
  }

  def replaceBead(row: Int, col: Int, bead: Bead): Matrix = {
    copy(matrix.updated(row, matrix(row).updated(col, bead)))
  }

  def changeStitch(newStitch: Stitch): Matrix = {
    new Matrix(matrix, newStitch)
  }

  val size: (Int, Int) = (matrix.size, matrix.head.size)

  def allBeads: Seq[Bead] = matrix.flatten

  def bead(row: Int, col: Int): Bead = matrix(row)(col)

  def fill(filling: Bead): Matrix = copy(
    Vector.tabulate(size._1, size._2) { (row, col) =>
      filling
    }
  )
}
