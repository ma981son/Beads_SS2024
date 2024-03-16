package de.htwg.se.beads.controller

import de.htwg.se.beads.model.{Grid}
import de.htwg.se.beads.util.{Command}

class ChangeGridSizeCommand(length: Int, width: Int, controller: Controller)
    extends Command {

  var memento: Grid = controller.grid

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
