package de.htwg.se.beads_controller.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.beads_util.util.{Observable, UndoManager}
import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads_controller.BeadsModule
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.model.gridComponent.BeadInterface
import de.htwg.se.beads.model.fileIOComponent.FileIOInterface
import com.google.inject.Inject
import com.google.inject.Guice
import scalafx.scene.paint.Color

class Controller @Inject() (var grid: GridInterface)
    extends Observable
    with ControllerInterface {

  private val undoManager: UndoManager = new UndoManager

  val injector = Guice.createInjector(new BeadsModule)
  val fileIo = injector.getInstance(classOf[FileIOInterface])

  def gridLength: Int = grid.length

  def gridWidth: Int = grid.width

  def gridStitch: Stitch = { grid.stitch }

  def bead(row: Int, col: Int): BeadInterface = { grid.bead(row, col) }

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

  def save(): Unit = {
    fileIo.save(grid)
    notifyObservers()
  }

  def load(): Unit = {
    grid = fileIo.load
    notifyObservers()
  }
}
