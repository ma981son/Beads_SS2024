package de.htwg.se.beads_util.Enums

import io.AnsiColor._
import scalafx.scene.paint.Color

enum DefaultColors(val color: Color) {
  case NoColor extends DefaultColors(Color.Transparent)
}

object ColorConverter {
  val stringToAnsi = Map(
    "black" -> BLACK_B,
    "red" -> RED_B,
    "green" -> GREEN_B,
    "yellow" -> YELLOW_B,
    "blue" -> BLUE_B,
    "magenta" -> MAGENTA_B,
    "cyan" -> CYAN_B,
    "white" -> WHITE_B
  )
  val rgbToAnsi = Map(
    Color.Black -> BLACK_B,
    Color.Red -> RED_B,
    Color.Green -> GREEN_B,
    Color.Yellow -> YELLOW_B,
    Color.Blue -> BLUE_B,
    Color.Magenta -> MAGENTA_B,
    Color.Cyan -> CYAN_B,
    Color.White -> WHITE_B
  )

  def colorToRGBA(color: Color): String = {
    f"rgba(${(color.getRed() * 255).toInt}%03d, ${(color
        .getGreen() * 255).toInt}%03d, ${(color
        .getBlue() * 255).toInt}%03d, ${color.getOpacity().toDouble})"
  }
  def rgbaToColor(rgba: String): Color = {
    val rgbaRegex =
      """rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*([\d.]+)\)""".r
    rgba match {
      case rgbaRegex(r, g, b, a) =>
        Color.rgb(r.toInt, g.toInt, b.toInt, a.toDouble)
      case _ =>
        throw new IllegalArgumentException("Invalid RGBA format")
    }
  }
}

object SelectedColor {
  private var currentColor: Color = DefaultColors.NoColor.color
  def setColor(color: Color): Unit = {
    currentColor = color
  }
  def getColor: Color = currentColor
  def getColorRGBA: String = ColorConverter.colorToRGBA(currentColor)
}
