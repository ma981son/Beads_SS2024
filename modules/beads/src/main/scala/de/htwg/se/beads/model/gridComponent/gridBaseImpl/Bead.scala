package de.htwg.se.beads.model.gridComponent.gridBaseImpl

import de.htwg.se.beads.model.gridComponent.BeadInterface
import de.htwg.se.beads_util.Enums.{DefaultColors, ColorConverter}
import scala.io.AnsiColor.RESET
import scalafx.scene.paint.Color

final case class Bead(
    beadCoord: Coord = Coord(0, 0),
    beadColor: Color = DefaultColors.NoColor.color
) extends BeadInterface {

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
    beadColor match {
      case color if ColorConverter.rgbToAnsi.contains(color) =>
        val ansiColor = ColorConverter.rgbToAnsi(color)
        s"|$ansiColor   ${RESET}|"
      case color =>
        val rgba = ColorConverter.colorToRGBA(color)
        s"|$rgba|"
    }
  }
}
