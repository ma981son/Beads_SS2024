package de.htwg.se.beads.model
import io.AnsiColor._

object stringToAnsi {
  val colors = Map(
    "black" -> BLACK_B,
    "red" -> RED_B,
    "green" -> GREEN_B,
    "yellow" -> YELLOW_B,
    "blue" -> BLUE_B,
    "magenta" -> MAGENTA_B,
    "cyan" -> CYAN_B,
    "white" -> WHITE_B
  )
}

object rgbToAnsi {
  val colors = Map(
    Color(0, 0, 0) -> BLACK_B,
    Color(255, 0, 0) -> RED_B,
    Color(0, 255, 0) -> GREEN_B,
    Color(255, 255, 0) -> YELLOW_B,
    Color(0, 0, 255) -> BLUE_B,
    Color(255, 0, 255) -> MAGENTA_B,
    Color(0, 255, 255) -> CYAN_B,
    Color(255, 255, 255) -> WHITE_B
  )
}

final case class Color(r: Double, g: Double, b: Double) {

  require(r >= 0 && r <= 255, "Red value must be between 0 and 255")
  require(g >= 0 && g <= 255, "Green value must be between 0 and 255")
  require(b >= 0 && b <= 255, "Blue value must be between 0 and 255")

  override def toString(): String = {
    "Color(" + r + "," + g + "," + b + ")"
  }
}
