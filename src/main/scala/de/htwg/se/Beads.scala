import de.htwg.se.model.Bead
import de.htwg.se.model.Coord
import de.htwg.se.model.Stitch
import de.htwg.se.model.Color

@main def beads: Unit = {
  println(bead)
}

def bead = Bead(Coord(1, 1), Stitch.Brick, Color(255, 255, 255))
