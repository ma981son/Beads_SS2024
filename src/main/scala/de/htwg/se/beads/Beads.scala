import de.htwg.se.beads.model.Bead
import de.htwg.se.beads.model.Coord
import de.htwg.se.beads.model.Enums.Stitch
import de.htwg.se.beads.model.Color
import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.Enums.DefaultColors
import de.htwg.se.beads.model.aview.Tui
import scala.io.AnsiColor._
import scala.io.StdIn.readLine

object Beads_Object {

  var grid = new Grid(10, 5)
  val tui = new Tui

  @main def beads: Unit = {
    var input: String = ""
    var inputSize: String = ""
    println("Change color: x-coord y-coord color")
    println("Change Grid size: size row-number col-number")
    println("Create new Template: n")
    println("Available Colors:")
    print(s"${BLACK}black${RESET}, ${RED}red${RESET}, ${GREEN}green${RESET}, ")
    print(
      s"${YELLOW}yellow${RESET}, ${BLUE}blue${RESET}, ${MAGENTA}magenta${RESET}, "
    )
    print(s"${CYAN}cyan${RESET}, ${WHITE}white${RESET}\n")

    print("Enter Template length and witdth: ")
    inputSize = readLine()
    grid = tui.processInputSizeLine(inputSize, grid)

    while (input != "q") {
      println("Grid: " + grid.toString())
      input = readLine();
      grid = tui.processInputLine(input, grid);
    }
  }
}
