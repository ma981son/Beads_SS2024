package de.htwg.se.beads_persistence.database

import de.htwg.se.beads.model.gridComponent.GridInterface
import play.api.libs.json.JsValue
import scala.util.Try

trait DAO_Interface {
  def save(grid: GridInterface): Unit

  def load(): Try[JsValue]

  def update(grid: GridInterface): Unit

}
