package de.htwg.se.model

import scala.collection.immutable.Vector

final case class Matrix(matrix: Vector[Vector[Bead]]) {

  def this(row: Int = 0, col: Int = 0, startBead: Bead) =
    this(Vector.tabulate(row, col) { (x, y) =>
      Bead(Coord(x, y), startBead.beadStitch, startBead.beadColor)
    })

  def replaceBead(row: Int, col: Int, cell: Bead): Matrix =
    copy(matrix.updated(row, matrix(row).updated(col, cell)))

  val size: (Int, Int) = (matrix.size, matrix.head.size)
  def bead(row: Int, col: Int): Bead = matrix(row)(col)

  def fill(filling: Bead): Matrix = copy(
    Vector.tabulate(this.size._1, this.size._2) { (row, col) =>
      filling
    }
  )
}
