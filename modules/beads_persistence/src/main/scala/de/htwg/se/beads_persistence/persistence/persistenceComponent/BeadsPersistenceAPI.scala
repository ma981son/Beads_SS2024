package de.htwg.se.beads_persistence.persistence.persistenceComponent

import de.htwg.se.beads_persistence.database.DAO_Interface
import de.htwg.se.beads_persistence.database.slick.BeadsSlickDB
import akka.stream.scaladsl._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route

import akka.stream.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.StandardRoute
import scala.util.Try
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import play.api.libs.json.JsValue
import akka.util.ByteString
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import play.api.libs.json.Json
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}
import scala.concurrent.Future
import de.htwg.se.beads_persistence.database.slick.tables.Grid
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO
import de.htwg.se.beads.model.gridComponent.GridInterface

class BeadsPersistenceAPI(
    dao: DAO_Interface = BeadsSlickDB
) {

  implicit val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "BeadsPersistenceAPI")
  implicit val executionContext: ExecutionContext = system.executionContext

  private val fileIO = new FileIO

  val routes: Route =
    concat(
      get {
        pathSingleSlash {
          complete("Beads Persistence API Service is online")
        }
      },
      get {
        path("persistence" / Segment) { command =>
          command match {
            case "load" =>
              dao.load() match {
                case Success(grid) =>
                  complete(status = 200, Json.prettyPrint(grid))
                case Failure(exception) => complete(500, exception.getMessage())
              }
          }
        }
      },
      post {
        path("persistence" / "save") {
          entity(as[String]) { saveRequest =>
            val json = Json.parse(saveRequest)
            val grid: GridInterface = fileIO.jsonToGrid(json)
            dao.save(grid)
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                """{"status": "Grid Saved"}"""
              )
            )
          }
        }
      },
      post {
        path("persistence" / "update") {
          entity(as[String]) { updateRequest =>
            val json = Json.parse(updateRequest)
            val grid: GridInterface = fileIO.jsonToGrid(json)
            dao.update(grid)
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                """{"status": "Grid Updated"}"""
              )
            )
          }
        }
      }
    )

  def start(): Unit = {
    val binding = Http().newServerAt("0.0.0.0", 3001).bind(routes)

    binding.onComplete {
      case Success(serverBinding) =>
        println(
          "BeadsPersistenceAPI connected successfully at http://localhost:3001/"
        )
        complete(status = 200, serverBinding.toString())
      case Failure(exception) =>
        complete(status = 500, exception.getMessage)
    }
  }

  def stop(): Unit = {
    system.terminate()
  }

}
