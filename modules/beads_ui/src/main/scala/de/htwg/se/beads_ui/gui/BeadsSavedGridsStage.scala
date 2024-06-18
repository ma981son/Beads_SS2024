package de.htwg.se.beads_ui.gui

import scalafx.stage.Stage
import scalafx.collections.ObservableBuffer
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import scalafx.scene.control.ListView
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.control.Label
import scalafx.geometry.Pos
import play.api.libs.json.JsValue
import scalafx.scene.control.Button
import scalafx.scene.layout.HBox
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.control.Tooltip
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.geometry.Insets

class BeadsSavedGridsStage(controller: ControllerInterface) extends Stage {

  title = "Saved Grids"

  icons += new Image(
    getClass
      .getResource(
        "/assets/icons/filter_vintage_24dp_FILL0_wght500_GRAD0_opsz24.png"
      )
      .toString()
  )

  val savedGrids: Seq[(JsValue)] = controller.loadAll()

  val gridIds: ObservableBuffer[String] = ObservableBuffer(
    savedGrids.map { grid =>
      val gridID = grid.\(0).as[Int]
      s"Grid ID $gridID"
    }*
  )

  val listView = new ListView[String] {
    items = gridIds
  }
  val loadButton = new Button {
    styleClass += "toolbarButton"

    this.text = "Load"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/download_24dp_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      ) {
        fitWidth = 24
        fitHeight = 24
      }
    }

    tooltip = new Tooltip("Load")

    onMouseClicked = (event: MouseEvent) => {
      loadSelectedGrid()
    }
  }

  val deleteButton = new Button {
    styleClass += "toolbarButton"

    this.text = "Delete"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/delete_24dp_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      ) {
        fitWidth = 24
        fitHeight = 24
      }
    }

    tooltip = new Tooltip("Delete")

    onMouseClicked = (event: MouseEvent) => {
      deleteSelectedGrid()
    }
  }

  val buttonBox = new HBox {
    padding = Insets(10, 10, 10, 10)
    spacing = 10
    children = Seq(loadButton, deleteButton)
  }

  scene = new Scene {
    root = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label("Saved Grids:"),
        listView,
        buttonBox
      )
    }
    stylesheets += getClass
      .getResource(
        "/assets/css/styles.css"
      )
      .toExternalForm
  }

  private def loadSelectedGrid(): Unit = {
    val selectedIndex = listView.selectionModel().getSelectedIndex().intValue
    if (selectedIndex >= 0 && selectedIndex < savedGrids.length) {
      val selectedGrid = savedGrids(selectedIndex)
      controller.load(selectedGrid.\(0).as[Int])
      close()
    }
  }

  private def deleteSelectedGrid(): Unit = {
    val selectedIndex = listView.selectionModel().getSelectedIndex().intValue
    if (selectedIndex >= 0 && selectedIndex < savedGrids.length) {
      val selectedGrid = savedGrids(selectedIndex)
      controller.delete(selectedGrid.\(0).as[Int])
      close()
    }
  }

}
