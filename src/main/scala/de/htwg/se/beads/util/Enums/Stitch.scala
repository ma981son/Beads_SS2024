package de.htwg.se.beads.util.Enums

enum Stitch {
  case Brick
  case Square
  case Fringe
}

object stringToStitch {
  val stitches = Map(
    "brick" -> Stitch.Brick,
    "square" -> Stitch.Square,
    "fringe" -> Stitch.Fringe
  )

  def stringToStitch(stringValue: String): Stitch =
    stringValue match
      case "Square" => Stitch.Square
      case "Brick"  => Stitch.Brick
      case "Fringe" => Stitch.Fringe
      case _        => Stitch.Square

}
