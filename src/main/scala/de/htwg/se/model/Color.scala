package de.htwg.se.model

final case class Color(r: Double, g: Double, b: Double) {
  override def toString(): String = {
    "Color(" + r + "," + g + "," + b + ")"
  }
}
