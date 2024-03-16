import de.htwg.se.beads.model.Bead
import de.htwg.se.beads.model.Coord
import de.htwg.se.beads.model.Enums.Stitch
import de.htwg.se.beads.model.Color
import de.htwg.se.beads.model.Grid
import de.htwg.se.beads.model.Enums.DefaultColors

@main def beads: Unit = {
  println(grid)
  println(
    grid
      .setBeadColor(1, 1, black)
      .changeSize(3, 3)
  )
}

def bead = Bead(Coord(1, 1), DefaultColors.NoColor.color)
val red = Color(255, 0, 0)
val black = Color(0, 0, 0)
val grid = new Grid(2, 2).setBeadColor(0, 0, black)
