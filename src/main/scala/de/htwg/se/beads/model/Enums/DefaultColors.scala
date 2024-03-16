package de.htwg.se.beads.model.Enums

import de.htwg.se.beads.model.Color

enum DefaultColors(val color: Color) {
  case NoColor extends DefaultColors(Color(255, 255, 255))
}
