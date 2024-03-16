package de.htwg.se.beads.model

import de.htwg.se.beads.model.BeadVector
import de.htwg.se.beads.model.Enums.Stitch
import de.htwg.se.beads.model.Enums.DefaultColors
final case class Grid(beads: Matrix) {

  def this(
      length: Int,
      width: Int,
      startColor: Color = DefaultColors.NoColor.color,
      stitch: Stitch = Stitch.Square
  ) = this(
    new Matrix(
      length,
      width,
      startColor,
      stitch
    )
  )

  val size_rows: Int = beads.size._1
  val size_cols: Int = beads.size._2

  def changeSize(row: Int, col: Int): Grid = {
    copy(beads = beads.changeSize(row, col))
  }

  val stitch: Stitch = beads.stitch

  def changeStitch(stitch: Stitch): Grid = {
    copy(beads.changeStitch(stitch))
  }

  def allBeads(): Seq[Bead] = beads.allBeads

  def bead(row: Int, col: Int): Bead = beads.bead(row, col)

  def setBeadColor(row: Int, col: Int, color: Color): Grid = {
    val oldbead = bead(row, col)
    copy(
      beads.replaceBead(
        row,
        col,
        Bead(oldbead.beadCoord, color)
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