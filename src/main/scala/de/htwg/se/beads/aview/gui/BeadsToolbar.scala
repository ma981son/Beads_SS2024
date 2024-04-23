package de.htwg.se.beads.aview.gui

import de.htwg.se.beads.controller.controllerComponent.ControllerInterface
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
import de.htwg.se.beads.util.Enums.stringToStitch.stringToStitch
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight

class BeadsToolbar(controller: ControllerInterface) extends VBox {

  styleClass += "toolbar"

  // Undo + Redo
  val undoButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/undo_FILL0_wght400_GRAD0_opsz24.png")
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
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/redo_FILL0_wght400_GRAD0_opsz24.png")
          )
          .toString()
      )
    }

    tooltip = new Tooltip("Redo")

    onMouseClicked = (event: MouseEvent) => {
      controller.redo()
    }
  }

  // Fill Grid
  val fillGridButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/format_color_fill_FILL0_wght400_GRAD0_opsz24.png")
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
    controller.changeGridStitch(stringToStitch(currentStitchOption.value))
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
  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_RESET = "\u001B[0m"

  val sizeButton = new Button {
    styleClass += "toolbarButton"

    graphic = new ImageView {
      image = new Image(
        getClass
          .getResource(
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/resize_FILL1_wght400_GRAD0_opsz24.png")
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
        controller.changeGridSize(width, length)
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
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/library_add_FILL1_wght400_GRAD0_opsz24.png")
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
          stringToStitch(currentStitchOption.value)
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
      new Separator {
        orientation = Orientation.Vertical
      },
      stitchChoiceBox,
      new Separator {
        orientation = Orientation.Vertical
      },
      widthTextField,
      lengthTextField,
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

  def setOrientation(orientation: Orientation): Unit = {
    children.clear()
    orientation match
      case Orientation.Horizontal =>
        children = List(toolBarFlowPlane)
        alignment = Pos.CenterLeft
        vgrow = Priority.Always

      case Orientation.Vertical => // TODO: Add Vertical Orientation -> Make Draggable
      case null =>
  }

  setOrientation(Orientation.Horizontal)

}
