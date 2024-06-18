package de.htwg.se.beads_ui

import de.htwg.se.beads_ui.gui.BeadsScene
import de.htwg.se.beads_ui.gui.BeadsScene
import de.htwg.se.beads_controller.BeadsModule
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_ui.gui.BeadsScene
import de.htwg.se.beads_ui.tui.Tui
import scala.io.StdIn.readLine
import scalafx.application.JFXApp3
import scala.io.AnsiColor._
import scalafx.application.Platform
import com.google.inject.Guice
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import scala.concurrent.ExecutionContext
import de.htwg.se.beads_controller.controller.BeadsControllerAPIModule
import de.htwg.se.beads_util.Enums.Event
import scalafx.Includes._
import scalafx.scene.image.Image

class BeadsUI(using controller: ControllerInterface) extends JFXApp3 {

  override def start(): Unit = {

    // GUI
    val stage = new JFXApp3.PrimaryStage {
      title = "Beads"
      icons += new Image(
        getClass
          .getResource(
            "/assets/icons/filter_vintage_24dp_FILL0_wght500_GRAD0_opsz24.png"
          )
          .toString()
      )
      scene = BeadsScene.createScene(controller)
    }
    Platform.runLater {
      stage.show()
      controller.notifyObservers(Event.GRID)
    }

    // TUI
    new Thread(() => {
      val tui = new Tui(controller)
      controller.notifyObservers(Event.GRID)

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
