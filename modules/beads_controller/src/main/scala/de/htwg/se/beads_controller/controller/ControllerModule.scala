package de.htwg.se.beads_controller.controller

import de.htwg.se.beads_controller.BeadsModule
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_controller.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.beads_controller.controller.controllerComponent.controllerRestImpl.BeadsControllerAPI
import de.htwg.se.beads_controller.controller.controllerComponent.controllerRestImpl.RestController
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import com.google.inject.Guice

object ControllerModule:
  val injector = Guice.createInjector(new BeadsModule)
  given ControllerInterface = injector.getInstance(classOf[ControllerInterface])

object RestControllerModule:
  given ControllerInterface = RestController()

object BeadsControllerAPIModule extends App:
  BeadsControllerAPI(using ControllerModule.given_ControllerInterface).start()
