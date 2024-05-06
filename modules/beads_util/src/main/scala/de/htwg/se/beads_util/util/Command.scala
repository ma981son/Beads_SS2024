package de.htwg.se.beads_util.util

trait Command {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}
