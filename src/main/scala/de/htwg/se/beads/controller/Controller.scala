package de.htwg.se.beads.controller

import de.htwg.se.beads.model.Color
import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.util.Enums.Stitch
import de.htwg.se.beads.util.Observable

final class Controller(var grid: Grid) extends Observable {

  def createEmptyGrid(length: Int, width: Int, stitch: Stitch): Unit = {
    grid = new Grid(
      length,
      width,
      startColor = Color(255, 255, 255),
      stitch = stitch
    )
    notifyObservers
  }

  def setBeadColor(row: Int, col: Int, color: Color): Unit = {
    grid = grid.setBeadColor(row, col, color)
    notifyObservers
  }

  def changeGridSize(length: Int, width: Int): Unit = {
    grid = grid.changeSize(length, width)
    notifyObservers
  }

  def changeGridStitch(stitch: Stitch): Unit = {
    grid = grid.changeStitch(stitch)
    notifyObservers
  }

  def GridToString: String = grid.toString()
}
