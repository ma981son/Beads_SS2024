package de.htwg.se.beads_ui.gui

import de.htwg.se.beads_util.Enums.SelectedColor
import scalafx.scene.control.ColorPicker
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.event.ActionEvent

class BeadsColorPicker extends ColorPicker {

  styleClass += "toolbarButton"

  value = SelectedColor.getColor

  onAction = (_: ActionEvent) => {
    SelectedColor.setColor(value())
  }

}
