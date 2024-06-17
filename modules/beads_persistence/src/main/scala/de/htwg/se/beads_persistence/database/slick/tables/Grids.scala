package de.htwg.se.beads_persistence.database.slick.tables

import play.api.libs.json.JsValue
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcType
import play.api.libs.json.Json
import slick.lifted.ProvenShape

case class Grid(id: Int, gridData: JsValue)

class Grids(tag: Tag) extends Table[Grid](tag, "GRIDS") {
  import JsonImplicits._

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def gridData: Rep[JsValue] = column[JsValue]("GRID_DATA")
  def * : ProvenShape[Grid] = (id, gridData).mapTo[Grid]
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
