package de.htwg.se.beads.controller

import de.htwg.se.beads.model.{Grid}
import de.htwg.se.beads.util.Command
import scalafx.scene.paint.Color

class SetBeadColorCommand(
    row: Int,
    col: Int,
    color: Color,
    controller: Controller
) extends Command {
  var memento: Grid = controller.grid

  override def doStep(): Unit = controller.grid =
    controller.grid.setBeadColor(row, col, color)

  override def undoStep(): Unit = {
    val oldcolor = memento.bead(row, col).beadColor
    controller.grid = controller.grid.setBeadColor(row, col, oldcolor)
  }

  override def redoStep(): Unit = controller.grid =
    controller.grid.setBeadColor(row, col, color)
}
