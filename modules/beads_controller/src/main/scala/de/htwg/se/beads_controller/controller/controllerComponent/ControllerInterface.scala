package de.htwg.se.beads_controller.controller.controllerComponent

import de.htwg.se.beads_util.util.Observable
import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads.model.gridComponent.BeadInterface
import scalafx.scene.paint.Color
import play.api.libs.json.JsValue
import de.htwg.se.beads.model.gridComponent.GridInterface

trait ControllerInterface extends Observable {
  var grid: GridInterface

  def gridLength: Int

  def gridWidth: Int

  def gridStitch: Stitch

  def createEmptyGrid(length: Int, width: Int, stitch: Stitch): Unit

  def bead(row: Int, col: Int): BeadInterface

  def beadToJson(row: Int, col: Int): JsValue

  def setBeadColor(row: Int, col: Int, color: Color): Unit

  def changeGridSize(length: Int, width: Int): Unit

  def changeGridStitch(stitch: Stitch): Unit

  def fillGrid(color: Color): Unit

  def gridToString: String

  def gridToJson: JsValue

  def undo(): Unit

  def redo(): Unit

  def save(): Unit

  def load(id: Int): Unit

  def loadAll(): Seq[JsValue]

  def delete(id: Int): Unit
}
