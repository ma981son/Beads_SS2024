package de.htwg.se.beads.model

import de.htwg.se.beads.util.Enums.DefaultColors
import scala.io.AnsiColor.RESET
import scalafx.scene.paint.Color
import de.htwg.se.beads.aview.gui.SelectedColor

final case class Bead(
    beadCoord: Coord = Coord(0, 0),
    beadColor: Color = DefaultColors.NoColor.color
) {

  def isColored: Boolean = beadColor != DefaultColors.NoColor.color

  def changeColor(color: Color): Bead = copy(beadCoord, color)

  override def equals(that: Any): Boolean = that match {
    case that: Bead =>
      that.canEqual(this) && this.beadCoord.equals(
        that.beadCoord
      ) && this.beadColor.equals(
        that.beadColor
      )
    case _ => false
  }

  override def toString: String = {
    val ansiColor = rgbToAnsi.colors.getOrElse(
      beadColor,
      SelectedColor.colorToRGBA(beadColor)
    )
    s"|$ansiColor   ${RESET}|"
  }

}
