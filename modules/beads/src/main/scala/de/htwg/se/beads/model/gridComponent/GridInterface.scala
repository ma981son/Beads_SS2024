package de.htwg.se.beads.model.gridComponent

import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.{Bead, BeadVector}
import scalafx.scene.paint.Color

trait GridInterface {
  def length: Int

  def width: Int

  def stitch: Stitch

  def changeGrid(l: Int, w: Int, stitch: Stitch): GridInterface

  def changeSize(row: Int, col: Int): GridInterface

  def changeStitch(stitch: Stitch): GridInterface

  def allBeads(): Seq[Bead]

  def bead(row: Int, col: Int): BeadInterface

  def setBeadColor(row: Int, col: Int, color: Color): GridInterface

  def fillGrid(color: Color): GridInterface

  def row(row: Int): BeadVector

  def col(col: Int): BeadVector

  def toString: String
}

trait BeadInterface {
  def isColored: Boolean

  def beadColor: Color

  def changeColor(color: Color): BeadInterface

  override def equals(that: Any): Boolean

  override def toString: String
}
