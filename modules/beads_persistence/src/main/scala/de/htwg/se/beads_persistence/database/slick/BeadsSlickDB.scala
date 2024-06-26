package de.htwg.se.beads_persistence.database.slick

import de.htwg.se.beads_persistence.database.DAO_Interface
import de.htwg.se.beads.model.gridComponent.GridInterface
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO
import play.api.libs.json.JsValue
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import de.htwg.se.beads_persistence.database.slick.tables.Grids.grids
import scala.concurrent.Await
import scala.util.control.Breaks._
import play.api.libs.json.JsValue
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType
import de.htwg.se.beads_persistence.database.slick.tables.Grids
import scala.concurrent.duration._
import de.htwg.se.beads_persistence.database.slick.tables.Grid
import de.htwg.se.beads_persistence.database.slick.tables.JsonImplicits._

object BeadsSlickDB extends DAO_Interface {

  private val connectionRetryAttempts = 5
  private val maxWaitSeconds = 10.seconds

  private val fileIO = new FileIO

  val db = Database.forURL(
    url = "jdbc:postgresql://postgres_db:5432/beads_postgres_db",
    user = "postgres",
    password = "beads_postgres_password",
    driver = "org.postgresql.Driver"
  )

  init(
    DBIO.seq(
      grids.schema.createIfNotExists
    )
  )

  private def init(setup: DBIOAction[Unit, NoStream, Effect.Schema]): Unit =
    println("Connection to Beads_Database...")
    breakable {
      for (i <- 1 to connectionRetryAttempts) {
        Try(Await.result(db.run(setup), maxWaitSeconds)) match {
          case Success(_) => {
            println("DB connection established")
            break
          }
          case Failure(e) => {
            if (e.getMessage.contains("Multiple primary key defined")) {
              println("Assuming DB connection established")
              break
            } else {
              println(
                s"DB connection failed - retrying... - $i/$connectionRetryAttempts"
              )
              println(e.getMessage)
              if (i != connectionRetryAttempts) {
                Thread.sleep(maxWaitSeconds.toMillis)
              } else {
                println("Max retry attempts reached. Connection failed.")
              }
            }
          }
        }
      }
    }

  override def save(grid: GridInterface): Try[Unit] = {
    val grids = TableQuery[Grids]
    val gridJson: JsValue = fileIO.gridToJson(grid)
    val insertAction =
      grids += Grid(0, gridJson)

    Try[Unit] { Await.result(db.run(insertAction), 10.seconds) }
  }

  override def load(id: Int): Try[JsValue] = {
    val grids = TableQuery[Grids]
    val query = grids.filter(_.id === id).result.headOption

    Try(Await.result(db.run(query), 10.seconds)) match {
      case Success(Some(grid)) => Success(grid.gridData)
      case Success(None)       => Failure(new Exception("No grid found"))
      case Failure(exception)  => Failure(exception)
    }
  }

  override def loadAll(): Try[Seq[(Int, JsValue)]] = {
    val grids = TableQuery[Grids]
    val query = grids.result

    Try(Await.result(db.run(query), 10.second)) match {
      case Success(gridSeq) =>
        Success(gridSeq.map(grid => (grid._1, grid._2)))
      case Failure(exception) => Failure(exception)
    }
  }

  override def update(id: Int, grid: GridInterface): Try[JsValue] = {
    val grids = TableQuery[Grids]
    val gridJson: JsValue = fileIO.gridToJson(grid)
    val updateAction =
      grids.filter(_.id === id).map(_.gridData).update(gridJson)

    val query = grids.filter(_.id === id).result.headOption

    Try(Await.result(db.run(query), 10.seconds)) match {
      case Success(Some(grid)) => Success(grid.gridData)
      case Success(None)       => Failure(new Exception("No grid found"))
      case Failure(exception)  => Failure(exception)
    }
  }

  override def delete(id: Int): Try[Unit] = {
    val grids = TableQuery[Grids]
    val deleteAction = grids.filter(_.id === id).delete

    Try[Unit] { Await.result(db.run(deleteAction), 10.seconds) }
  }

}
