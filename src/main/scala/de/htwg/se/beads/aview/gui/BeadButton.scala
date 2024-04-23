package de.htwg.se.beads.aview.gui

import de.htwg.se.beads.controller.controllerComponent.ControllerInterface
import scalafx.scene.control.Button
import scalafx.scene.layout.Border
import scalafx.scene.layout.BorderStroke
import scalafx.scene.input.MouseEvent
import scalafx.Includes._

object BeadButton {
  val BEAD_BUTTON_HEIGHT: Double = 50.0
  val BEAD_BUTTON_WIDTH: Double = 50.0

  val defaultStyle: String =
    "-fx-border-color: #a1a2a7; -fx-border-width: 1px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-background-insets: 1px"
}

class BeadButton(row: Int, col: Int, controller: ControllerInterface)
    extends Button {

  def bead = controller.bead(row, col)

  prefHeight = BeadButton.BEAD_BUTTON_HEIGHT
  prefWidth = BeadButton.BEAD_BUTTON_WIDTH

  style =
    s"-fx-background-color: ${SelectedColor.colorToRGBA(bead.beadColor)}; ${BeadButton.defaultStyle}"

  def update(): Unit = {
    style =
      s"-fx-background-color: ${SelectedColor.colorToRGBA(bead.beadColor)}; ${BeadButton.defaultStyle}"
  }

  onMousePressed = (event: MouseEvent) => {
    controller.setBeadColor(row, col, SelectedColor.getColor)
    update()
  }

}
