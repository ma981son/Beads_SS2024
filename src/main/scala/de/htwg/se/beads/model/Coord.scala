package de.htwg.se.beads.model

final case class Coord(x: Double, y: Double) {
  override def toString: String = x + "," + y
}
