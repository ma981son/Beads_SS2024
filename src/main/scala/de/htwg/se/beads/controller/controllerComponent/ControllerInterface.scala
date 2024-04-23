package de.htwg.se.beads.controller.controllerComponent

import de.htwg.se.beads.util.Enums.stringToStitch.stitches
import de.htwg.se.beads.util.Enums.Stitch
import scalafx.scene.paint.Color
import scala.swing.event.Event
import de.htwg.se.beads.util.Observable
import de.htwg.se.beads.model.gridComponent.BeadInterface

trait ControllerInterface extends Observable {
  def gridLength: Int

  def gridWidth: Int

  def gridStitch: Stitch

  def bead(row: Int, col: Int): BeadInterface

  def createEmptyGrid(length: Int, width: Int, stitch: Stitch): Unit

  def setBeadColor(row: Int, width: Int, color: Color): Unit

  def changeGridSize(length: Int, width: Int): Unit

  def changeGridStitch(stitch: Stitch): Unit

  def fillGrid(color: Color): Unit

  def GridToString: String

  def undo(): Unit

  def redo(): Unit
}

class BeadChanged extends Event
case class GridSizeChanged(length: Int, width: Int) extends Event
case class GridStitchChanged(stitch: Stitch) extends Event
case class GridColorChanged(color: Color) extends Event
case class NewGridCreated(length: Int, width: Int, stitch: Stitch) extends Event
