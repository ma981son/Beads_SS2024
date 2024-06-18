package de.htwg.se.beads_persistence.database

import de.htwg.se.beads.model.gridComponent.GridInterface
import play.api.libs.json.JsValue
import scala.util.Try

trait DAO_Interface {
  def save(grid: GridInterface): Try[Unit]

  def load(id: Int): Try[JsValue]

  def loadAll(): Try[Seq[(Int, JsValue)]]

  def update(id: Int, grid: GridInterface): Try[JsValue]

  def delete(id: Int): Try[Unit]
}
