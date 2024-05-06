package de.htwg.se.beads_controller.controller.controllerComponent

import de.htwg.se.beads_util.util.Observable
import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads.model.gridComponent.BeadInterface
import scalafx.scene.paint.Color

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

  def save(): Unit

  def load(): Unit
}
