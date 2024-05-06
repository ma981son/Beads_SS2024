package de.htwg.se.beads_controller.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.beads_util.util.Command
import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads.model.gridComponent.GridInterface

class CreateGridCommand(
    length: Int,
    width: Int,
    stitch: Stitch,
    controller: Controller
) extends Command {

  var memento: GridInterface = controller.grid

  override def doStep(): Unit = {
    memento = controller.grid
    val newGrid = controller.grid.changeGrid(length, width, stitch)
    controller.grid = newGrid
  }

  override def undoStep(): Unit = {
    val new_memento = controller.grid
    controller.grid = memento
    memento = new_memento
  }

  override def redoStep(): Unit = {
    val new_memento = controller.grid
    controller.grid = memento
    memento = new_memento
  }
}
