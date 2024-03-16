package de.htwg.se.beads.model
import de.htwg.se.beads.model.Enums.DefaultColors

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
    "| " + beadColor + " |"
  }

}
