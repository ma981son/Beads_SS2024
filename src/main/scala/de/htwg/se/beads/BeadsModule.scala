package de.htwg.se.beads

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.beads.util.Enums.Stitch
import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.beads.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads.controller.controllerComponent.controllerBaseImpl.Controller
import com.google.inject.name.Names
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Matrix
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Bead
import de.htwg.se.beads.util.Enums.DefaultColors
import com.google.inject.Provides
import de.htwg.se.beads.model.fileIOComponent.FileIOInterface
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO

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
