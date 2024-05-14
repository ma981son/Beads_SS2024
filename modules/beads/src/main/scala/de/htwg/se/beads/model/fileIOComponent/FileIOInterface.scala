package de.htwg.se.beads.model.fileIOComponent

import de.htwg.se.beads.model.gridComponent.GridInterface
import play.api.libs.json.JsValue
import de.htwg.se.beads.model.gridComponent.BeadInterface

trait FileIOInterface {
  def load: GridInterface
  def save(grid: GridInterface): Unit
  def gridToJson(grid: GridInterface): JsValue
  def jsonToGrid(json: JsValue): GridInterface
  def beadToJson(bead: BeadInterface): JsValue
  def jsonToBead(json: JsValue): BeadInterface
}
