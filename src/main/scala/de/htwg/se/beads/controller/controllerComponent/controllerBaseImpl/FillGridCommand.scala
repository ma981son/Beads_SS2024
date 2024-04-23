package de.htwg.se.beads.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.util.{Command}
import scalafx.scene.paint.Color

class FillGridCommand(color: Color, controller: Controller) extends Command {

  var memento: GridInterface = controller.grid

  override def doStep(): Unit = {
    memento = controller.grid
    val newtemp = controller.grid.fillGrid(color)
    controller.grid = newtemp
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
