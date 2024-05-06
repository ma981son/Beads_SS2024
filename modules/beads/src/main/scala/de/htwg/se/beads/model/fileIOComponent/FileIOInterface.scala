package de.htwg.se.beads.model.fileIOComponent

import de.htwg.se.beads.model.gridComponent.GridInterface

trait FileIOInterface {
  def load: GridInterface
  def save(grid: GridInterface): Unit
}
