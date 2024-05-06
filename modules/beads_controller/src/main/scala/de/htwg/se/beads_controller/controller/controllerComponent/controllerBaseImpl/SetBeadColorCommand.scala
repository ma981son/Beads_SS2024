package de.htwg.se.beads_controller.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.beads_util.util.Command
import de.htwg.se.beads.model.gridComponent.GridInterface
import scalafx.scene.paint.Color

class SetBeadColorCommand(
    row: Int,
    col: Int,
    color: Color,
    controller: Controller
) extends Command {
  var memento: GridInterface = controller.grid

  override def doStep(): Unit = controller.grid =
    controller.grid.setBeadColor(row, col, color)

  override def undoStep(): Unit = {
    val oldcolor = memento.bead(row, col).beadColor
    controller.grid = controller.grid.setBeadColor(row, col, oldcolor)
  }

  override def redoStep(): Unit = controller.grid =
    controller.grid.setBeadColor(row, col, color)
}
