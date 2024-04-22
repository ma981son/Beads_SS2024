package de.htwg.se.beads.model
import io.AnsiColor._
import scalafx.scene.paint.Color

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
    Color.Black -> BLACK_B,
    Color.Red -> RED_B,
    Color.Green -> GREEN_B,
    Color.Yellow -> YELLOW_B,
    Color.Blue -> BLUE_B,
    Color.Magenta -> MAGENTA_B,
    Color.Cyan -> CYAN_B,
    Color.White -> WHITE_B
  )
}
