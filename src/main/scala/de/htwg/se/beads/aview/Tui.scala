package de.htwg.se.beads.model.aview

import scala.io.StdIn.readLine
import de.htwg.se.beads.model.{Grid, rgbToAnsi, stringToAnsi}
import de.htwg.se.beads.controller.Controller
import de.htwg.se.beads.util.Observer
import de.htwg.se.beads.util.Enums.{Stitch, stringToStitch, DefaultColors}
import de.htwg.se.beads.model.rgbToAnsi.colors
import scala.io.AnsiColor.WHITE
import de.htwg.se.beads.model.Color

class Tui(controller: Controller) extends Observer {

  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_RESET = "\u001B[0m"

  controller.add(this)

  def processInputSizeLine(input: String): Unit = {
    input.split(" ").toList match {

      case List(row, column, stitchName)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        val stitch = stringToStitch.stitches.getOrElse(
          stitchName.toLowerCase,
          Stitch.Square
        )
        controller.createEmptyGrid(
          row.toInt,
          column.toInt,
          stitch = stitch
        )

      case List(row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        controller.createEmptyGrid(
          row.toInt,
          column.toInt,
          stitch = Stitch.Square
        )

      case _ =>
        println(
          s"${ANSI_YELLOW}Incorrect input format. Defaulting to Square stitch"
        )
    }
  }

  def processInputLine(input: String): Unit = {
    input.split(" ").toList match {

      case List("size", row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        controller.changeGridSize(row.toInt, column.toInt)
      case List("stitch", stitchName) =>
        val stitch = stringToStitch.stitches.getOrElse(
          stitchName.toLowerCase,
          Stitch.Square
        )
        controller.changeGridStitch(stitch)

      case List(row, column, color)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        val ansiColor =
          stringToAnsi.colors.getOrElse(
            color, {
              println(
                s"${ANSI_YELLOW}Warning: Color '$color' is not recognized.${ANSI_RESET}"
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

      case List("n") =>
        print("Enter Grid length and width: ")
        val inputSize = readLine()
        processInputSizeLine(inputSize)
      case _ =>
    }
  }

  override def update: Unit = println(controller.GridToString)
}
