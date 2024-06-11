package de.htwg.se.beads_persistence.database.slick.tables

import play.api.libs.json.JsValue
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcType
import play.api.libs.json.Json

case class Grid(id: Int, gridData: JsValue)

class Grids(tag: Tag) extends Table[Grid](tag, "grids") {
  import JsonImplicits._

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def gridData = column[JsValue]("grid_data")
  def * = (id, gridData).mapTo[Grid]
}

object Grids {
  val grids = TableQuery[Grids]
}

object JsonImplicits {
  implicit val jsValueColumnType: JdbcType[JsValue] =
    MappedColumnType.base[JsValue, String](
      json => Json.stringify(json),
      str => Json.parse(str)
    )
}
