package de.htwg.se.beads.model.aview

import scala.io.StdIn.readLine
import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.stringToAnsi
import de.htwg.se.beads.model.rgbToAnsi

class Tui {

  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_RESET = "\u001B[0m"

  def processInputSizeLine(input: String, grid: Grid): Grid = {
    input.split(" ").toList match {
      case List(row, column)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        val Array(row, column) = input.split(" ")
        List(row, column)
          .filter(t => t != ' '.toString())
          .map(t => t.toInt) match {

          case row :: column :: Nil => grid.changeSize(row, row)
          case _                    => grid

        }
      case _ => grid
    }
  }

  def processInputLine(input: String, grid: Grid): Grid = {
    input.split(" ").toList match {
      case List(command, row, column)
          if (command.matches("size") && row
            .matches("\\d+") && column.matches("\\d+")) =>
        grid.changeSize(row.toInt, column.toInt)
      case List(row, column, color)
          if (row.matches("\\d+") && column.matches("\\d+")) =>
        val ansiColor = stringToAnsi.colors.getOrElse(
          color, {
            println()
            println(
              s"${ANSI_YELLOW}Warning: Color '$color' is not recognized.${ANSI_RESET}"
            )
            println()
            -1
          }
        )
        if (ansiColor != -1) {
          grid.setBeadColor(
            row.toInt,
            column.toInt,
            rgbToAnsi.colors.map(_.swap)(ansiColor)
          )
        } else {
          grid
        }
      case List("n") =>
        print("Enter Grid length and width: ")
        val inputSize = readLine()
        processInputSizeLine(inputSize, grid)
      case _ =>
        grid
    }
  }
}
