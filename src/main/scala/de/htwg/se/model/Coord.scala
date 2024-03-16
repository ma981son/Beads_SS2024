package de.htwg.se.model

final case class Coord(x: Double, y: Double) {
  override def toString: String = x + "," + y
}
