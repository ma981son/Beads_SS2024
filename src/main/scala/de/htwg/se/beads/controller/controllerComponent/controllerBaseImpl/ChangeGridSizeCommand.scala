package de.htwg.se.beads.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.beads.util.{Command}
import de.htwg.se.beads.model.gridComponent.GridInterface

class ChangeGridSizeCommand(length: Int, width: Int, controller: Controller)
    extends Command {

  var memento: GridInterface = controller.grid

  override def doStep(): Unit = {
    memento = controller.grid
    val newGrid = controller.grid.changeSize(length, width)
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
