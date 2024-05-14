package de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl

import de.htwg.se.beads.model.fileIOComponent.FileIOInterface
import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.beads_util.Enums.*
import play.api.libs.json._
import scala.io.Source
import com.google.inject.Guice
import scalafx.scene.paint.Color
import de.htwg.se.beads.model.gridComponent.BeadInterface
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Bead
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Coord

class FileIO() extends FileIOInterface {

  override def load: GridInterface = {
    val source: String = Source.fromFile("temp.json").getLines.mkString
    val json: JsValue = Json.parse(source)

    val length = (json \ "temp" \ "length").as[Int]
    val width = (json \ "temp" \ "width").as[Int]
    val stitch = (json \ "temp" \ "stitch").as[String]

    var grid = new Grid(
      length,
      width,
      Color.Black,
      StitchConverter.stringToStitch
        .getOrElse(stitch.toLowerCase(), Stitch.Square)
    )

    (json \ "temp" \ "beads").as[List[JsObject]].foreach { beadJson =>
      val row = (beadJson \ "row").as[Int]
      val col = (beadJson \ "col").as[Int]
      val color = (beadJson \ "color").as[Color]
      grid = grid.setBeadColor(row, col, color)
    }

    grid
  }

  override def save(grid: GridInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("temp.json"))
    pw.write(Json.prettyPrint(gridToJson(grid)))
    pw.close
  }

  override def gridToJson(grid: GridInterface): JsValue = {
    Json.obj(
      "temp" -> Json.obj(
        "length" -> JsNumber(grid.length),
        "width" -> JsNumber(grid.width),
        "stitch" -> JsString(grid.stitch.toString()),
        "beads" -> Json.toJson(
          for {
            row <- 0 until grid.length;
            col <- 0 until grid.width
          } yield {
            beadToJson(grid.bead(row, col))
          }
        )
      )
    )
  }

  override def jsonToGrid(json: JsValue): Grid = {
    val length = (json \ "temp" \ "length").as[Int]
    val width = (json \ "temp" \ "width").as[Int]
    val stitch = (json \ "temp" \ "stitch").as[String]

    var grid = new Grid(
      length,
      width,
      Color.Black,
      StitchConverter.stringToStitch
        .getOrElse(stitch.toLowerCase(), Stitch.Square)
    )

    (json \ "temp" \ "beads").as[List[JsObject]].foreach { beadJson =>
      val row = (beadJson \ "row").as[Int]
      val col = (beadJson \ "col").as[Int]
      val color = (beadJson \ "color").as[Color]
      grid = grid.setBeadColor(row, col, color)
    }

    grid
  }

  override def beadToJson(bead: BeadInterface): JsValue = {
    Json.obj(
      "row" -> bead.beadCoord.x,
      "col" -> bead.beadCoord.y,
      "color" -> Json.toJson(bead.beadColor)
    )
  }

  override def jsonToBead(json: JsValue): BeadInterface = {
    val row = (json \ "row").as[Int]
    val col = (json \ "col").as[Int]
    val color = (json \ "color").as[Color]
    Bead(Coord(row, col), color)
  }

  implicit val colorWrites: Writes[Color] = new Writes[Color] {
    def writes(color: Color) = Json.obj(
      "red" -> (color.getRed() * 255).toInt,
      "green" -> (color.getGreen() * 255).toInt,
      "blue" -> (color.getBlue() * 255).toInt,
      "opacity" -> color.getOpacity()
    )
  }

  implicit val colorReads: Reads[Color] = new Reads[Color] {
    def reads(json: JsValue): JsResult[Color] = {
      val colorResult = for {
        red <- (json \ "red").validate[Int]
        green <- (json \ "green").validate[Int]
        blue <- (json \ "blue").validate[Int]
        opacity <- (json \ "opacity").validate[Double]
      } yield {
        Color.rgb(red, green, blue, opacity)
      }

      colorResult
    }
  }

}
