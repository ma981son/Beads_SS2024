package de.htwg.se.beads_ui.gui

import scalafx.stage.Stage
import scalafx.collections.ObservableBuffer
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import scalafx.scene.control.ListView
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.Label
import scalafx.geometry.Pos

class BeadsSavedGridsStage(controller: ControllerInterface) extends Stage {

  title = "Saved Grids"

  val gridData: ObservableBuffer[String] = ObservableBuffer(
    controller.loadAll().map(_.toString): _*
  )

  val listView = new ListView[String] {
    items = gridData
  }

  scene = new Scene {
    root = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label("Saved Grids:"),
        listView
      )
    }
  }
}
