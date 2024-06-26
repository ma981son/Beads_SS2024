package de.htwg.se.beads_ui.gui

import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.Enums.Stitch
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.control.ScrollPane
import scalafx.application.Platform
import scalafx.scene.transform.Scale
import scalafx.geometry.Insets
import scalafx.scene.layout.FlowPane
import scalafx.geometry.Pos
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.Border
import de.htwg.se.beads_util.Enums.Event

class BeadGrid(controller: ControllerInterface)
    extends StackPane
    with Observer {

  controller.add(this)

  private val inset = Insets(10, 10, 10, 10)

  val mainVBox = new VBox {
    padding_=(Insets(10, 10, 10, 10))
    alignment_=(Pos.Center)
  }

  val zoomableScrollPane = new ZoomableScrollPane(mainVBox)

  children = zoomableScrollPane
  margin = inset

  override def update(e: Event): Unit = {
    Platform.runLater(() => {
      e.match
        case Event.GRID =>
          mainVBox.children.clear()
          generateButtons()
    })
  }

  private def generateButtons(): Unit = {
    controller.gridStitch match {
      case Stitch.Brick => brickStrategy()
      case _            => squareStrategy()
    }
  }

  private def squareStrategy(): Unit = {
    for (row <- 0 until controller.gridLength) {
      val hBox = new HBox()
      for (col <- 0 until controller.gridWidth) {
        val beadButton = new BeadButton(
          row,
          col,
          controller
        )
        hBox.children.add(beadButton)
      }
      mainVBox.children.add(hBox)
    }
  }

  private def brickStrategy(): Unit = {
    for (row <- 0 until controller.gridLength) {
      val hBox = new HBox()
      if (row % 2 == 0) {
        hBox.padding = Insets(0, 0, 0, BeadButton.BEAD_BUTTON_WIDTH / 2)
      }
      for (col <- 0 until controller.gridWidth) {
        val beadButton = new BeadButton(
          row,
          col,
          controller
        )
        hBox.children.add(beadButton)
      }
      mainVBox.children.add(hBox)
    }
  }

}
