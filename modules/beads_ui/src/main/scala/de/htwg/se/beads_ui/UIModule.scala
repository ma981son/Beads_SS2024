package de.htwg.se.beads_ui

import de.htwg.se.beads_controller.controller.ControllerModule
import de.htwg.se.beads_controller.controller.RestControllerModule
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import scalafx.application.Platform

object UIModule {
  Starter(ControllerModule.given_ControllerInterface).start()
}

object UIRestModule {
  Starter(RestControllerModule.given_ControllerInterface).start()
}

class Starter(controller: ControllerInterface) {
  def start(): Unit = {
    val ui = new BeadsUI(using controller)
    ui.main(Array.empty)
  }
}
