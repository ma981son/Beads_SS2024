package de.htwg.se.beads.util.Enums

import scalafx.scene.paint.Color

enum DefaultColors(val color: Color) {
  case NoColor extends DefaultColors(Color.Transparent)
}
