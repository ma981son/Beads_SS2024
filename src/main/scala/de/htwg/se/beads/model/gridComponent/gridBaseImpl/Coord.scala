package de.htwg.se.beads.model.gridComponent.gridBaseImpl

final case class Coord(x: Double, y: Double) {
  override def toString: String = x + "," + y
}
