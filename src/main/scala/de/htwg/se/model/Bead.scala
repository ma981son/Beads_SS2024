package de.htwg.se.model

object Stitch extends Enumeration {
  val Brick, Square, Fringe = Value
}

final case class Bead(
    beadCoord: Coord,
    beadStitch: Stitch.Value,
    beadColor: Color = Color(255, 255, 255)
) {

  def isColored: Boolean = beadColor != Color(255, 255, 255)

  def changeColor(color: Color): Bead = copy(beadCoord, beadStitch, color)

  def changeStitch(newStitch: Stitch.Value): Bead =
    copy(beadCoord, newStitch, beadColor)

  override def equals(that: Any): Boolean = that match {
    case that: Bead =>
      that.canEqual(this) && this.beadCoord.equals(
        that.beadCoord
      ) && this.beadStitch.equals(that.beadStitch) && this.beadColor.equals(
        that.beadColor
      )
    case _ => false
  }

  def addBeadRight: Bead = {
    if (!beadStitch.equals(Stitch.Fringe)) {
      val newBead =
        Bead(Coord(beadCoord.x + 1, beadCoord.y), beadStitch, beadColor)
      return newBead
    }
    None.get
  }

  def addBeadUp: Bead = {
    if (!beadStitch.equals(Stitch.Fringe) && !beadStitch.equals(Stitch.Brick)) {
      val newBead =
        Bead(Coord(beadCoord.x, beadCoord.y + 1), beadStitch, beadColor)
      return newBead
    }
    None.get
  }

  def addBeadLeft: Bead = {
    if (!beadStitch.equals(Stitch.Fringe)) {
      return Bead(Coord(beadCoord.x - 1, beadCoord.y), beadStitch, beadColor)
    }
    None.get
  }

  override def toString: String = {
    val standard = Color(255.0, 255.0, 255.0).toString().size
    val beadsize = beadColor.toString().size
    if (beadsize < standard) {
      val space = (standard - beadsize) / 2
      "| " + "  " * space + beadColor + "  " * space + " |"
    }
    "| " + beadColor + " |"
  }

}
