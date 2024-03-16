import de.htwg.se.beads.model.Bead
import de.htwg.se.beads.model.Coord
import de.htwg.se.beads.model.Color
import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.aview.Tui
import scala.io.AnsiColor._
import scala.io.StdIn.readLine
import de.htwg.se.beads.controller.Controller

object Beads_Object {

  val controller = new Controller(new Grid(1, 6))
  val tui = new Tui(controller)
  controller.notifyObservers()

  @main def beads: Unit = {
    var input: String = ""
    var inputSize: String = ""
    println("Change color: x-coord y-coord color")
    println("Change Grid size: size row-number col-number")
    println("Change Grid stitch: stitch stitch-name")
    println("Change Grid color: fill color")
    println("Create new Template: n")
    println("Undo: z")
    println("Undo: y")
    println("Available Colors:")
    print(s"${BLACK}black${RESET}, ${RED}red${RESET}, ${GREEN}green${RESET}, ")
    print(
      s"${YELLOW}yellow${RESET}, ${BLUE}blue${RESET}, ${MAGENTA}magenta${RESET}, "
    )
    print(s"${CYAN}cyan${RESET}, ${WHITE}white${RESET}\n")

    print("Enter Template length, witdth and stitch: ")
    inputSize = readLine()
    tui.processInputSizeLine(inputSize)

    while (input != "q") {
      input = readLine();
      tui.processInputLine(input);
    }
  }
}
