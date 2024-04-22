import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.aview.{Tui}
import scala.io.StdIn.readLine
import de.htwg.se.beads.controller.Controller
import scalafx.application.JFXApp3
import de.htwg.se.beads.aview.gui.BeadsScene
import scala.io.AnsiColor._
import scalafx.application.Platform
import scalafx.scene.image.Image

object Beads_Object extends JFXApp3 {

  override def start(): Unit = {

    val controller = new Controller(new Grid(1, 6))

    // GUI
    val stage = new JFXApp3.PrimaryStage {
      title = "Beads"
      icons += new Image(
        getClass
          .getResource(
            ("/scala/de/htwg/se/beads/aview/gui/assets/icons/filter_vintage_FILL0_wght400_GRAD0_opsz48.png")
          )
          .toString()
      )
      scene = BeadsScene.createScene(controller)
    }
    Platform.runLater {
      stage.show()
      controller.notifyObservers()
    }

    // TUI
    new Thread(() => {
      val tui = new Tui(controller)
      controller.notifyObservers()

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
      print(
        s"${BLACK}black${RESET}, ${RED}red${RESET}, ${GREEN}green${RESET}, "
      )
      print(
        s"${YELLOW}yellow${RESET}, ${BLUE}blue${RESET}, ${MAGENTA}magenta${RESET}, "
      )
      print(s"${CYAN}cyan${RESET}, ${WHITE}white${RESET}\n")
      print("Enter Template length, width, and stitch: ")
      inputSize = scala.io.StdIn.readLine()
      tui.processInputSizeLine(inputSize)

      while (input != "q") {
        input = scala.io.StdIn.readLine()
        tui.processInputLine(input)
      }
    }).start()
  }
}
