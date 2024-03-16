package de.htwg.se.beads.model

final case class Color(r: Double, g: Double, b: Double) {

  require(r >= 0 && r <= 255, "Red value must be between 0 and 255")
  require(g >= 0 && g <= 255, "Green value must be between 0 and 255")
  require(b >= 0 && b <= 255, "Blue value must be between 0 and 255")

  override def toString(): String = {
    "Color(" + r + "," + g + "," + b + ")"
  }
}
