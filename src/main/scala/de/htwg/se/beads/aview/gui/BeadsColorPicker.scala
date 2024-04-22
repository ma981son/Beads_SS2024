package de.htwg.se.beads.aview.gui

import scalafx.scene.control.ColorPicker
import scalafx.scene.paint.Color
import de.htwg.se.beads.util.Enums.DefaultColors
import scalafx.Includes._
import scalafx.event.ActionEvent

object SelectedColor {
  private var currentColor: Color = DefaultColors.NoColor.color

  def setColor(color: Color): Unit = {
    currentColor = color
  }

  def getColor: Color = currentColor

  def getColorRGBA: String = colorToRGBA(currentColor)

  def colorToRGBA(color: Color): String = {
    f"rgba(${(color.getRed() * 255).toInt}%03d, ${(color
        .getGreen() * 255).toInt}%03d, ${(color
        .getBlue() * 255).toInt}%03d, ${color.getOpacity().toDouble})"
  }
}

class BeadsColorPicker extends ColorPicker {

  styleClass += "toolbarButton"

  onAction = (_: ActionEvent) => {
    SelectedColor.setColor(value())
  }

}
