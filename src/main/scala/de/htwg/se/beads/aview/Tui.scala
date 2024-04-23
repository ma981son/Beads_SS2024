package de.htwg.se.beads.model.aview

import scala.io.StdIn.readLine
import de.htwg.se.beads.model.{Grid, rgbToAnsi, stringToAnsi}
import de.htwg.se.beads.controller.Controller
import de.htwg.se.beads.util.Observer
import de.htwg.se.beads.util.Enums.{Stitch, stringToStitch, DefaultColors}
import de.htwg.se.beads.model.rgbToAnsi.colors
import scala.io.AnsiColor.WHITE
import scala.util.Try
import scala.util.{Success, Failure}

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_RESET = "\u001B[0m"

  private val warningIndexOutOfBounds =
    s"${ANSI_YELLOW}Warning: Bead index out of bounds.$ANSI_RESET"

  private val warningInvalidMatrixSize =
    s"${ANSI_YELLOW}Warning: Invalid Matrix Size.$ANSI_RESET"

  private val warningIncorrectInputFormat =
    s"${ANSI_YELLOW}Warning: Incorrect input format. Defaulting to Square stitch${ANSI_RESET}"

  private def getWarningStitchName: String => String = { stitchName =>
    s"${ANSI_YELLOW}Warning: Stitch '$stitchName' is not recognized.${ANSI_RESET}"
  }

  private def getWarningColor: String => String = { color =>
    s"${ANSI_YELLOW}Warning: Color '$color' is not recognized.${ANSI_RESET}"
  }

  def processInputSizeLine(input: String): Unit = {
    input.split(" ").toList match {

      case List(row, column, stitchName)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        Try {
          val stitch = stringToStitch.stitches.getOrElse(
            stitchName.toLowerCase,
            Stitch.Square
          )
          controller.createEmptyGrid(row.toInt, column.toInt, stitch = stitch)
        } match {
          case Success(_) =>
          case Failure(_: IndexOutOfBoundsException) =>
            println(warningIndexOutOfBounds)
          case Failure(_: IllegalArgumentException) =>
            println(warningInvalidMatrixSize)
          case Failure(_) =>
            println(warningIncorrectInputFormat)
        }

      case List(row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        Try {
          controller.createEmptyGrid(
            row.toInt,
            column.toInt,
            stitch = Stitch.Square
          )
        } match {
          case Success(_) =>
          case Failure(_: IndexOutOfBoundsException) =>
            println(warningIndexOutOfBounds)
          case Failure(_: IllegalArgumentException) =>
            println(warningInvalidMatrixSize)
          case Failure(_) => println(warningIncorrectInputFormat)
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
        Try {
          controller.changeGridSize(row.toInt, column.toInt)
        } match {
          case Success(_) =>
          case Failure(_: IndexOutOfBoundsException) =>
            println(warningIndexOutOfBounds)
          case Failure(_) => println(warningIncorrectInputFormat)
        }

      case List("stitch", stitchName) =>
        val stitch = stringToStitch.stitches.getOrElse(
          stitchName.toLowerCase, {
            println(
              getWarningStitchName(stitchName)
            )
            Stitch.Square
          }
        )
        controller.changeGridStitch(stitch)

      case List("fill", color) =>
        val ansiColor = stringToAnsi.colors.getOrElse(
          color, {
            println(
              getWarningColor(color)
            )
            WHITE
          }
        )
        controller.fillGrid(
          rgbToAnsi.colors
            .map(_.swap)
            .getOrElse(ansiColor, DefaultColors.NoColor.color)
        )

      case List(row, column, color)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        Try {
          val ansiColor =
            stringToAnsi.colors.getOrElse(
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
            rgbToAnsi.colors
              .map(_.swap)
              .getOrElse(ansiColor, DefaultColors.NoColor.color)
          )
        } match {
          case Success(_) =>
          case Failure(_: IndexOutOfBoundsException) =>
            println(warningIndexOutOfBounds)
          case Failure(_) => println(warningIncorrectInputFormat)
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
