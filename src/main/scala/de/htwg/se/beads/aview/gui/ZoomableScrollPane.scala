package de.htwg.se.beads.aview.gui

import scalafx.scene.Node
import scalafx.scene.control.ScrollPane
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.geometry.Pos
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.AnchorPane
import scalafx.geometry.Insets
import scalafx.scene.layout.FlowPane
import scalafx.geometry.Bounds
import scalafx.geometry.BoundingBox
import scalafx.scene.layout.Pane
import scalafx.scene.layout.VBox
import scalafx.scene.input.ZoomEvent
import scalafx.scene.input.ScrollEvent
import scalafx.scene.input.TouchEvent

class ZoomableScrollPane(target: Node) extends ScrollPane {
  private var scaleValue = 1.0
  private val minScaleValue = 0.25
  private val maxScaleValue = 2.0
  private val zoomIntensityMouse = 0.02
  private val zoomIntensityTouch = 0.005
  private var isTouchEvent = false

  private val inset = Insets(10, 10, 10, 10)

  private val stackpane = new Group {
    children = target
  }

  private val zoomLevelLabel = new Label {
    text = "Zoom: 100%"
  }

  content = outerNode(stackpane)
  pannable = true
  hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  fitToHeight = true
  fitToWidth = true
  pannable = true

  addEventHandler(
    ScrollEvent.ANY,
    (event: ScrollEvent) => {
      onScroll(event.getTextDeltaY, event.getX, event.getY)
    }
  )

  addEventHandler(
    ZoomEvent.ZOOM,
    (event: ZoomEvent) => {
      onZoom(event.totalZoomFactor, event.x, event.y)
    }
  )

  addEventHandler(
    TouchEvent.ANY,
    (event: TouchEvent) => {
      isTouchEvent = true
    }
  )

  private def onScroll(deltaY: Double, x: Double, y: Double): Unit = {
    val zoomIntensity =
      if (isTouchEvent) zoomIntensityTouch else zoomIntensityMouse
    val zoomFactor = Math.exp(deltaY * zoomIntensity)
    scaleValue *= zoomFactor
    scaleValue = scaleValue max minScaleValue min maxScaleValue
    isTouchEvent = false
    updateScale()
  }

  private def onZoom(zoomFactor: Double, x: Double, y: Double): Unit = {
    val zoomIntensity =
      if (isTouchEvent) zoomIntensityTouch else zoomIntensityMouse
    scaleValue *= Math.pow(zoomFactor, zoomIntensity)
    scaleValue = scaleValue max minScaleValue min maxScaleValue
    isTouchEvent = false
    updateScale()
  }

  private def updateScale(): Unit = {
    target.scaleX = scaleValue
    target.scaleY = scaleValue

    // Update the zoom level label (optional)
    val zoomPercentage = (scaleValue * 100).toInt
    zoomLevelLabel.text = s"Zoom: $zoomPercentage%"
  }

  private def centeredNode(node: Node): Node = {
    val vBox = new VBox(node) {
      alignment_=(Pos.Center)
      styleClass += "beadGrid"
    }
    vBox
  }

  private def outerNode(node: Node): Node = {
    val outerNode = centeredNode(node)
    outerNode.onScroll = (event: ScrollEvent) => {
      event.consume()
      onScroll(event.getTextDeltaY(), event.x, event.y)
    }
    outerNode
  }

}
