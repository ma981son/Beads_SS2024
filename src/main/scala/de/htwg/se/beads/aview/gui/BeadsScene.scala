package de.htwg.se.beads.aview.gui

import de.htwg.se.beads.controller.Controller
import scalafx.Includes._
import scalafx.scene.{Scene}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.input.MouseEvent
import scalafx.scene.control.Button
import scalafx.scene.control.Separator
import scalafx.geometry.Pos
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.Priority
import scalafx.scene.layout.HBox
import scalafx.geometry.Orientation

object BeadsScene {

  def createScene(controller: Controller): Scene = {
    val borderPane = new BorderPane
    val beadGrid = new BeadGrid(controller)

    borderPane.center = beadGrid
    borderPane.top = new BeadsToolbar(controller)

    val scene = new Scene {
      root = borderPane
      stylesheets += getClass
        .getResource(
          "/scala/de/htwg/se/beads/aview/gui/css/styles.css"
        )
        .toExternalForm
    }

    scene
  }
}
