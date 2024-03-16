package de.htwg.se.model

import de.htwg.se.model.BeadVector
final case class Grid(beads: Matrix) {
  def this(length: Int, width: Int) = this(
    new Matrix(
      length,
      width,
      Bead(Coord(0, 0), Stitch.Square, Color(255, 255, 255))
    )
  )

  val size_rows: Int = beads.size._1
  val size_cols: Int = beads.size._2

  def bead(row: Int, col: Int): Bead = beads.bead(row, col)

  def setColor(row: Int, col: Int, color: Color): Grid = {
    val oldbead = bead(row, col)
    copy(
      beads.replaceBead(
        row,
        col,
        Bead(oldbead.beadCoord, oldbead.beadStitch, color)
      )
    )
  }

  def row(row: Int): BeadVector = BeadVector(beads.matrix(row))

  def col(col: Int): BeadVector = BeadVector(beads.matrix.map(row => row(col)))

  override def toString: String = {
    val regex = "x".r
    val line = "x" * size_cols + "\n"
    var lineseparator =
      ("-" * beads.bead(0, 0).toString.size) * size_cols + "\n"
    var box = "\n" + (lineseparator + ((line + lineseparator) * size_rows))

    for (row <- 0 until size_rows) {
      for (col <- 0 until size_cols) {
        lineseparator = ("-" * bead(
          size_rows - 1,
          size_cols - 1
        ).toString.size) * size_cols + "\n"
        box = regex.replaceFirstIn(box, bead(row, col).toString)
      }
    }
    box
  }
}
