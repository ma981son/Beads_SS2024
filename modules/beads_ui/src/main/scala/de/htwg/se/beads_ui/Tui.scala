package de.htwg.se.beads_ui

import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.Enums.{
  Stitch,
  StitchConverter,
  ColorConverter,
  DefaultColors
}
import scala.io.StdIn.readLine
import scala.io.AnsiColor.WHITE

class Tui(controller: ControllerInterface) extends Observer {

  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_RESET = "\u001B[0m"

  private val warningIndexOutOfBounds =
    s"${ANSI_YELLOW}Warning: Bead index out of bounds.$ANSI_RESET"

  private val warningInvalidMatrixSize =
    s"${ANSI_YELLOW}Warning: Invalid Matrix Size.$ANSI_RESET"

  private val warningIncorrectInputFormat =
    s"${ANSI_YELLOW}Warning: Incorrect input format. Defaulting to Square stitch${ANSI_RESET}"

  private def getWarningStitchName(stitchName: String): String = {
    s"${ANSI_YELLOW}Warning: Stitch '$stitchName' is not recognized.${ANSI_RESET}"
  }

  private def getWarningColor(color: String): String = {
    s"${ANSI_YELLOW}Warning: Color '$color' is not recognized.${ANSI_RESET}"
  }

  controller.add(this)

  def processInputSizeLine(input: String): Unit = {
    input.split(" ").toList match {

      case List(row, column, stitchName)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        try {
          val stitch = StitchConverter.stringToStitch.getOrElse(
            stitchName.toLowerCase,
            Stitch.Square
          )
          controller.createEmptyGrid(
            row.toInt,
            column.toInt,
            stitch = stitch
          )
        } catch {
          case _: IndexOutOfBoundsException =>
            println(warningIndexOutOfBounds)
          case _: IllegalArgumentException =>
            println(warningInvalidMatrixSize)
        }

      case List(row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        try {
          controller.createEmptyGrid(
            row.toInt,
            column.toInt,
            stitch = Stitch.Square
          )
        } catch {
          case _: IndexOutOfBoundsException =>
            println(warningIndexOutOfBounds)
          case _: IllegalArgumentException =>
            println(warningInvalidMatrixSize)
        }

      case _ =>
        println(
          warningIncorrectInputFormat
        )
    }
  }

  def processInputLine(input: String): Unit = {
    input.split(" ").toList match {

      case List("size", row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        try {
          controller.changeGridSize(row.toInt, column.toInt)
        } catch {
          case _: IndexOutOfBoundsException =>
            println(warningIndexOutOfBounds)
        }

      case List("stitch", stitchName) =>
        val stitch = StitchConverter.stringToStitch.getOrElse(
          stitchName.toLowerCase, {
            println(
              getWarningStitchName(stitchName)
            )
            Stitch.Square
          }
        )
        controller.changeGridStitch(stitch)

      case List("fill", color) =>
        val ansiColor = ColorConverter.stringToAnsi.getOrElse(
          color, {
            println(
              getWarningColor(color)
            )
            WHITE
          }
        )
        controller.fillGrid(
          ColorConverter.rgbToAnsi
            .map(_.swap)
            .getOrElse(ansiColor, DefaultColors.NoColor.color)
        )

      case List(row, column, color)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        try {
          val ansiColor =
            ColorConverter.stringToAnsi.getOrElse(
              color, {
                println(
                  getWarningColor(color)
                )
                WHITE
              }
            )
          controller.setBeadColor(
            row.toInt,
            column.toInt,
            ColorConverter.rgbToAnsi
              .map(_.swap)
              .getOrElse(ansiColor, DefaultColors.NoColor.color)
          )
        } catch {
          case _: IndexOutOfBoundsException =>
            println(warningIndexOutOfBounds)
        }

      case List("z") => controller.undo()

      case List("y") => controller.redo()

      case List("n") =>
        print("Enter Template length, width, and stitch: ")
        val inputSize = readLine()
        processInputSizeLine(inputSize)

      case _ =>
    }
  }

  override def update(): Boolean = {
    println(controller.GridToString)
    true
  }
}
