package de.htwg.se.beads.controller

import de.htwg.se.beads.model.{Color, Grid}
import de.htwg.se.beads.util.Enums.Stitch
import de.htwg.se.beads.util.{Observable, UndoManager}

final class Controller(var grid: Grid) extends Observable {

  private val undoManager: UndoManager = new UndoManager

  def createEmptyGrid(length: Int, width: Int, stitch: Stitch): Unit = {
    undoManager.doStep(CreateGridCommand(length, width, stitch, this))
    notifyObservers()
  }

  def setBeadColor(row: Int, col: Int, color: Color): Unit = {
    undoManager.doStep(SetBeadColorCommand(row, col, color, this))
    notifyObservers()
  }

  def changeGridSize(length: Int, width: Int): Unit = {
    undoManager.doStep(ChangeGridSizeCommand(length, width, this))
    notifyObservers()
  }

  def changeGridStitch(stitch: Stitch): Unit = {
    undoManager.doStep(ChangeGridStitchCommand(stitch, this))
    notifyObservers()
  }

  def fillGrid(color: Color): Unit = {
    undoManager.doStep(FillGridCommand(color, this))
    notifyObservers()
  }

  def GridToString: String = grid.toString()

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }
}
