package de.htwg.se.beads.aview.gui

import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.BorderPane
import scalafx.geometry.Orientation
import scalafx.scene.layout.Pane
import de.htwg.se.beads.aview.gui.SelectedOrientation.setOrientation

object SelectedOrientation {
  var currentOrientation: Orientation = Orientation.Horizontal

  def setOrientation(orientation: Orientation): Unit = {
    currentOrientation = orientation
  }

  def getOrientation(): Orientation = {
    currentOrientation
  }

}

object DraggablePane {
  private val dragThreshold = 50
  private var isDragging = false
  private var startX: Double = 0
  private var startY: Double = 0

  def makeDraggable(pane: Pane, borderPane: BorderPane): Pane = {
    pane.onMousePressed = (event: MouseEvent) => {
      startX = event.sceneX
      startY = event.sceneY
    }

    pane.onMouseDragged = (event: MouseEvent) => {
      if (
        !isDragging &&
        (math.abs(event.sceneX - startX) > dragThreshold ||
          math.abs(event.sceneY - startY) > dragThreshold)
      ) {
        isDragging = true
      }

      if (isDragging) {
        val newX = event.sceneX - startX
        val newY = event.sceneY - startY

        val sceneWidth = pane.scene().getWidth
        val sceneHeight = pane.scene().getHeight

        val leftDistance = newX
        val rightDistance = sceneWidth - (newX + pane.width.value)
        val topDistance = newY
        val bottomDistance = sceneHeight - (newY + pane.height.value)

        val minDistance =
          List(leftDistance, rightDistance, topDistance, bottomDistance).min

        if (minDistance == leftDistance) {
          setOrientation(Orientation.Vertical)
          borderPane.setTop(null)
          borderPane.setBottom(null)
          borderPane.setRight(null)
          borderPane.setLeft(pane)
        } else if (minDistance == rightDistance) {
          setOrientation(Orientation.Vertical)
          borderPane.setTop(null)
          borderPane.setBottom(null)
          borderPane.setLeft(null)
          borderPane.setRight(pane)
        } else if (minDistance == topDistance) {
          setOrientation(Orientation.Horizontal)
          borderPane.setRight(null)
          borderPane.setLeft(null)
          borderPane.setBottom(null)
          borderPane.setTop(pane)
        } else {
          setOrientation(Orientation.Horizontal)
          borderPane.setRight(null)
          borderPane.setLeft(null)
          borderPane.setTop(null)
          borderPane.setBottom(pane)
        }

        pane.translateX = newX
        pane.translateY = newY
      }
    }

    pane.onMouseReleased = (event: MouseEvent) => {
      if (isDragging) {
        isDragging = false
      }
    }
    pane
  }
}
