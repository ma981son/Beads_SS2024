package de.htwg.se.beads.model

import de.htwg.se.beads.model.BeadVector
import de.htwg.se.beads.util.Enums.*

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

  def changeGrid(l: Int, w: Int, stitch: Stitch): Grid = copy(
    new Matrix(l, w, stitch = stitch)
  )

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

  def fillGrid(color: Color): Grid = {
    copy(beads = new Matrix(size_rows, size_cols, color, stitch))
  }

  def row(row: Int): BeadVector = BeadVector(beads.matrix(row))

  def col(col: Int): BeadVector = BeadVector(beads.matrix.map(row => row(col)))

  object String {

    var strategy = if (stitch.equals(Stitch.Brick)) strategy1 else strategy2

    def strategy1: String = {
      val regex = "x".r
      val line = "x" * size_cols + "\n"
      var lineseparator = ("-" * 6) * size_cols + "\n"
      lineseparator = "---" + lineseparator
      val line1 = " " * 3 + line
      var box =
        "\n" + (lineseparator + (line1 + lineseparator + line + lineseparator) * (size_rows / 2))
      if (size_rows % 2 != 0) {
        box = box + line1 + lineseparator
      }
      for (row <- 0 until size_rows) {
        for (col <- 0 until size_cols) {
          box = regex.replaceFirstIn(box, bead(row, col).toString)
        }
      }
      box
    }

    def strategy2: String = {
      val regex = "x".r
      val line = "x" * size_cols + "\n"
      var lineseparator = ("-" * 6) * size_cols + "\n"
      var box = "\n" + (lineseparator + (line + lineseparator) * size_rows)
      for (row <- 0 until size_rows) {
        for (col <- 0 until size_cols) {
          box = regex.replaceFirstIn(box, bead(row, col).toString)
        }
      }
      box
    }
  }

  override def toString: String = {
    String.strategy
  }
}
