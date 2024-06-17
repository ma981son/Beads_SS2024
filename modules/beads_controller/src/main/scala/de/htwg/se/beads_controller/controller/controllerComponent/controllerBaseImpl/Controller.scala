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
import play.api.libs.json.JsValue
import de.htwg.se.beads_util.Enums.Event

class Controller @Inject() () extends Observable with ControllerInterface {

  private val undoManager: UndoManager = new UndoManager

  val injector = Guice.createInjector(new BeadsModule)
  val fileIo = injector.getInstance(classOf[FileIOInterface])

  var grid: GridInterface = injector.getInstance(classOf[GridInterface])
  def gridLength: Int = grid.length

  def gridWidth: Int = grid.width

  def gridStitch: Stitch = { grid.stitch }

  def bead(row: Int, col: Int): BeadInterface = { grid.bead(row, col) }

  def beadToJson(row: Int, col: Int): JsValue =
    fileIo.beadToJson(bead(row, col))

  def createEmptyGrid(length: Int, width: Int, stitch: Stitch): Unit = {
    undoManager.doStep(CreateGridCommand(length, width, stitch, this))
    notifyObservers(Event.GRID)
  }

  def setBeadColor(row: Int, col: Int, color: Color): Unit = {
    undoManager.doStep(SetBeadColorCommand(row, col, color, this))
    notifyObservers(Event.GRID)
  }

  def changeGridSize(length: Int, width: Int): Unit = {
    undoManager.doStep(ChangeGridSizeCommand(length, width, this))
    notifyObservers(Event.GRID)
  }

  def changeGridStitch(stitch: Stitch): Unit = {
    undoManager.doStep(ChangeGridStitchCommand(stitch, this))
    notifyObservers(Event.GRID)
  }

  def fillGrid(color: Color): Unit = {
    undoManager.doStep(FillGridCommand(color, this))
    notifyObservers(Event.GRID)
  }

  def gridToString: String = grid.toString()

  def gridToJson: JsValue = fileIo.gridToJson(grid)

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers(Event.GRID)
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers(Event.GRID)
  }

  def save(): Unit = {
    fileIo.save(grid)
    notifyObservers(Event.GRID)
  }

  def load(id: Int): Unit = {
    grid = fileIo.load
    notifyObservers(Event.GRID)
  }

  def loadAll(): Seq[JsValue] = {
    Seq.empty[JsValue]
  } // TODO Implement saving/loading multiple File in FileIO

  def delete(id: Int): Unit = {}
}
