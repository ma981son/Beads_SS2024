package de.htwg.se.beads_controller

import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_controller.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.beads_util.Enums.*
import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.beads.model.fileIOComponent.FileIOInterface
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Matrix
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import com.google.inject.Provides

class BeadsModule extends AbstractModule {

  val defaultLength: Int = 10
  val defaultWidth: Int = 10
  val defaultStitch: Stitch = Stitch.Square

  override def configure(): Unit = {
    bind(classOf[GridInterface]).to(classOf[Grid])
    bind(classOf[ControllerInterface]).to(classOf[Controller])
    bind(classOf[FileIOInterface]).to(classOf[FileIO])
  }

  @Provides
  def provideMatrix(): Matrix = {
    new Matrix(
      defaultLength,
      defaultWidth,
      DefaultColors.NoColor.color,
      Stitch.Square
    )
  }
}
