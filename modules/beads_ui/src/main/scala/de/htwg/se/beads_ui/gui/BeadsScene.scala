package de.htwg.se.beads_ui.gui

import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
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

  def createScene(controller: ControllerInterface): Scene = {
    val borderPane = new BorderPane
    val beadGrid = new BeadGrid(controller)

    borderPane.center = beadGrid
    borderPane.top = new BeadsToolbar(controller)

    val scene = new Scene {
      root = borderPane
      stylesheets += getClass
        .getResource(
          "/assets/css/styles.css"
        )
        .toExternalForm
    }

    scene
  }
}
