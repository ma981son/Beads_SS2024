package de.htwg.se.beads.model.gridComponent.gridBaseImpl

import de.htwg.se.beads.model.gridComponent.gridBaseImpl.BeadVector
import de.htwg.se.beads.util.Enums.*
import scalafx.scene.paint.Color
import de.htwg.se.beads.model.gridComponent.GridInterface
import com.google.inject.Inject
import com.google.inject.name.Named

case class Grid @Inject() (beads: Matrix) extends GridInterface {

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

  val length: Int = beads.size._1
  val width: Int = beads.size._2

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
    copy(beads = new Matrix(length, width, color, stitch))
  }

  def row(row: Int): BeadVector = BeadVector(beads.matrix(row))

  def col(col: Int): BeadVector = BeadVector(beads.matrix.map(row => row(col)))

  object String {

    val strategy = if (stitch.equals(Stitch.Brick)) strategy1 else strategy2

    def strategy1: String = {
      val regex = "x".r
      val line = "x" * width + "\n"
      var lineseparator = ("-" * 6) * width + "\n"
      lineseparator = "---" + lineseparator
      val line1 = " " * 3 + line
      var box =
        "\n" + (lineseparator + (line1 + lineseparator + line + lineseparator) * (length / 2))
      if (length % 2 != 0) {
        box = box + line1 + lineseparator
      }
      for (row <- 0 until length) {
        for (col <- 0 until width) {
          box = regex.replaceFirstIn(box, bead(row, col).toString)
        }
      }
      box
    }

    def strategy2: String = {
      val regex = "x".r
      val line = "x" * width + "\n"
      var lineseparator = ("-" * 6) * width + "\n"
      var box = "\n" + (lineseparator + (line + lineseparator) * length)
      for (row <- 0 until length) {
        for (col <- 0 until width) {
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
