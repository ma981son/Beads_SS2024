import de.htwg.se.beads_ui.gui.BeadsScene
import de.htwg.se.beads_ui.gui.BeadsScene
import de.htwg.se.beads_controller.BeadsModule
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_ui.gui.BeadsScene

import scala.concurrent.ExecutionContext
import de.htwg.se.beads_controller.controller.BeadsControllerAPIModule
import de.htwg.se.beads_ui.tui.Tui
import de.htwg.se.beads_ui.UIRestModule
import de.htwg.se.beads_ui.UIModule
import de.htwg.se.beads_persistence.persistence.BeadsPersistenceAPIModule

object Beads_Object {

  def main(args: Array[String]): Unit = {
    BeadsPersistenceAPIModule
    BeadsControllerAPIModule
    UIModule
  }
}
