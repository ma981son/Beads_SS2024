package de.htwg.se.beads.aview.gui

import de.htwg.se.beads.controller.Controller
import de.htwg.se.beads.util.Enums.Stitch
import de.htwg.se.beads.util.Observer
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

class BeadGrid(controller: Controller) extends StackPane with Observer {

  controller.add(this)

  private val inset = Insets(10, 10, 10, 10)

  val mainVBox = new VBox {
    padding_=(Insets(10, 10, 10, 10))
    alignment_=(Pos.Center)
  }

  val zoomableScrollPane = new ZoomableScrollPane(mainVBox)

  children = zoomableScrollPane
  margin = inset

  override def update(): Boolean = {
    Platform.runLater(() => {
      mainVBox.children.clear()
      generateButtons()
    })
    true
  }

  private def generateButtons(): Unit = {
    controller.grid.stitch match {
      case Stitch.Brick => brickStrategy()
      case _            => squareStrategy()
    }
  }

  private def squareStrategy(): Unit = {
    for (row <- 0 until controller.grid.size_rows) {
      val hBox = new HBox()
      for (col <- 0 until controller.grid.size_cols) {
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
    for (row <- 0 until controller.grid.size_rows) {
      val hBox = new HBox()
      if (row % 2 == 0) {
        hBox.padding = Insets(0, 0, 0, BeadButton.BEAD_BUTTON_WIDTH / 2)
      }
      for (col <- 0 until controller.grid.size_cols) {
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
