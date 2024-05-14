package de.htwg.se.beads_ui.gui

import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.Enums.{Stitch, StitchConverter, SelectedColor}
import scalafx.Includes._
import scalafx.scene.layout.VBox
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.control.Tooltip
import scalafx.scene.input.MouseEvent
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.StringProperty
import scalafx.scene.control.ChoiceBox
import scalafx.event.ActionEvent
import scalafx.scene.control.TextField
import scala.util.Try
import scalafx.scene.layout.FlowPane
import scalafx.geometry.Pos
import scalafx.geometry.Orientation
import scalafx.scene.layout.Priority
import scalafx.scene.control.Separator
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import de.htwg.se.beads_util.Enums.Event

class BeadsToolbar(controller: ControllerInterface) extends VBox with Observer {

  controller.add(this)

  styleClass += "toolbar"

  // Undo + Redo
  val undoButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/undo_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Undo")

    onMouseClicked = (event: MouseEvent) => {
      controller.undo()
    }
  }

  val redoButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/redo_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Redo")

    onMouseClicked = (event: MouseEvent) => {
      controller.redo()
    }
  }

  // Save + Load
  val saveButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/save_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Save")

    onMouseClicked = (event: MouseEvent) => {
      controller.save()
    }
  }

  val loadButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/download_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Load")

    onMouseClicked = (event: MouseEvent) => {
      controller.load()
    }
  }

  // Fill Grid
  val fillGridButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/format_color_fill_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Fill Grid")

    onMouseClicked = (event: MouseEvent) => {
      controller.fillGrid(SelectedColor.getColor)
    }
  }

  // ChoiceBox
  val stitchChoiceOptions = ObservableBuffer("Square", "Brick")
  var currentStitchOption = StringProperty(controller.gridStitch.toString())

  val stitchChoiceBox = new ChoiceBox[String](stitchChoiceOptions) {
    styleClass += "toolbarButton"
    value = currentStitchOption.value
  }

  stitchChoiceBox.onAction = (_: ActionEvent) => {
    currentStitchOption.value = stitchChoiceBox.value.value
    controller.changeGridStitch(
      StitchConverter.stringToStitch
        .getOrElse(currentStitchOption.value.toLowerCase(), Stitch.Square)
    )
  }

  // TextFields
  val widthTextField = new TextField {
    styleClass += "textField"
    promptText = "Enter width"
    prefColumnCount = 12
    font_=(Font.font("Courier New", FontWeight.BOLD, 14))
  }
  val lengthTextField = new TextField {
    styleClass += "textField"
    promptText = "Enter length"
    prefColumnCount = 12
    font_=(Font.font("Courier New", FontWeight.BOLD, 14))
  }

  def validateNumberInput(text: String): Boolean = {
    Try(text.toInt).filter(_ > 0).isSuccess
  }

  // Size Button
  private val ANSI_YELLOW = "/u001B[33m"
  private val ANSI_RESET = "/u001B[0m"

  val sizeButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/resize_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Resize Template")

    onMouseClicked = (event: MouseEvent) => {
      val widthText = widthTextField.text.value
      val lenghtText = lengthTextField.text.value
      if (validateNumberInput(widthText) && validateNumberInput(lenghtText)) {
        val width = widthText.toInt
        val length = lenghtText.toInt
        controller.changeGridSize(length, width)
      } else {
        println(s"${ANSI_YELLOW}Warning: Invalid Matrix Size.$ANSI_RESET")
      }
    }
  }

  // New Template
  val newTemplateButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/assets/icons/add_box_FILL0_wght500_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Create new Template")

    onMouseClicked = (event: MouseEvent) => {
      val widthText = widthTextField.text.value
      val lenghtText = lengthTextField.text.value
      if (validateNumberInput(widthText) && validateNumberInput(lenghtText)) {
        val width = widthText.toInt
        val length = lenghtText.toInt
        controller.createEmptyGrid(
          length,
          width,
          StitchConverter.stringToStitch
            .getOrElse(currentStitchOption.value.toLowerCase(), Stitch.Square)
        )
      } else {
        println(s"${ANSI_YELLOW}Warning: Invalid Matrix Size.$ANSI_RESET")
      }

    }
  }

  // ColorPicker
  val colorPicker = new BeadsColorPicker

  // ButtonsFlowPlane
  val toolBarFlowPlane = new FlowPane {
    children = Seq(
      undoButton,
      redoButton,
      saveButton,
      loadButton,
      new Separator {
        orientation = Orientation.Vertical
      },
      stitchChoiceBox,
      new Separator {
        orientation = Orientation.Vertical
      },
      lengthTextField,
      widthTextField,
      sizeButton,
      newTemplateButton,
      new Separator {
        orientation = Orientation.Vertical
      },
      fillGridButton,
      colorPicker
    )
    alignment = Pos.CenterLeft
    hgap = 5
    vgap = 5
  }

  def setOrientation(orientation: Option[Orientation]): Unit = {
    children.clear()
    orientation match
      case Some(Orientation.Horizontal) =>
        children = List(toolBarFlowPlane)
        alignment = Pos.CenterLeft
        vgrow = Priority.Always

      case Some(
            Orientation.Vertical
          ) => // TODO: Add Vertical Orientation -> Make Draggable
      case _ =>
  }

  setOrientation(Some(Orientation.Horizontal))

  override def update(e: Event): Unit = {
    e match {
      case Event.GRID =>
        stitchChoiceBox.value = controller.gridStitch.toString()
    }
  }

}
