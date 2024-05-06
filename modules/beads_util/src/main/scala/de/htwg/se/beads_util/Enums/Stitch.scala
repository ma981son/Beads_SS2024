package de.htwg.se.beads_util.Enums

enum Stitch {
  case Brick
  case Square
  case Fringe
}

object StitchConverter {
  val stringToStitch = Map(
    "brick" -> Stitch.Brick,
    "square" -> Stitch.Square,
    "fringe" -> Stitch.Fringe
  )
}
