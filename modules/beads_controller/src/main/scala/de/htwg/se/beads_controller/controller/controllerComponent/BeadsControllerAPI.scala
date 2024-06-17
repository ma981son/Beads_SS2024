package de.htwg.se.beads_controller.controller.controllerComponent

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
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_util.Enums.StitchConverter
import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads_util.Enums.ColorConverter.rgbaToColor
import scala.concurrent.Await
import scala.concurrent.duration.*
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.htwg.se.beads_util.util.Observer
import de.htwg.se.beads_util.Enums.Event
import akka.http.scaladsl.model.HttpMethod
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO

class BeadsControllerAPI(using controller: ControllerInterface) {

  private val persistenceServiceEndpoint =
    "http://persistence:3001/persistence"

  private val fileIO = new FileIO

  implicit val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "BeadsControllerAPI")
  implicit val executionContext: ExecutionContext = system.executionContext

  controller.add(DB_UpdateObserver)

  val routes: Route =
    concat(
      get {
        pathSingleSlash {
          complete("Beads API Service is online")
        }
      },
      get {
        path("controller" / Segment) { command =>
          command match {
            case "grid" =>
              completeWithData(Json.prettyPrint(controller.gridToJson))
            case "gridString" => completeWithData(controller.gridToString)
            case "gridLength" =>
              completeWithData(controller.gridLength.toString())
            case "gridWidth" =>
              completeWithData(controller.gridWidth.toString())
            case "gridStitch" =>
              completeWithData(controller.gridStitch.toString())
            case "undo" =>
              controller.undo()
              completeWithData(Json.prettyPrint(controller.gridToJson))
            case "redo" =>
              controller.redo()
              completeWithData(Json.prettyPrint(controller.gridToJson))
            case "save" =>
              save match {
                case Success(_) =>
                  completeWithData("success")
                case Failure(exception) =>
                  println(s"Request failed: ${exception}")
                  failWith(exception)
              }
            case "loadAll" =>
              loadAll() match {
                case Success(seqJson) =>
                  complete(
                    HttpEntity(
                      ContentTypes.`application/json`,
                      Json.toJson(seqJson).toString()
                    )
                  )
                case Failure(exception) =>
                  println(s"Request failed: ${exception}")
                  failWith(exception)
              }
          }
        }
      },
      get {
        path("controller" / "load" / IntNumber) { id =>
          load(id) match {
            case Success(json) =>
              controller.grid = fileIO.jsonToGrid(json)
              completeWithData(controller.gridToJson.toString)
            case Failure(exception) =>
              println(s"Request failed: ${exception}")
              failWith(exception)
          }
        }
      },
      post {
        path("controller" / Segment) { command =>
          implicit val jsValueUnmarshaller: Unmarshaller[HttpEntity, JsValue] =
            Unmarshaller.byteStringUnmarshaller.mapWithCharset {
              (data, charset) =>
                Json.parse(data.decodeString(charset.nioCharset.name))
            }
          entity(as[JsValue]) { jsValue =>
            command match {
              case "createEmptyGrid" =>
                controller.createEmptyGrid(
                  (jsValue \ "length").as[Int],
                  (jsValue \ "width").as[Int],
                  StitchConverter.stringToStitch
                    .getOrElse(
                      ((jsValue \ "stitch").as[String]).toLowerCase,
                      Stitch.Square
                    )
                )
                completeWithData(controller.gridToJson.toString)
              case "changeGridSize" =>
                controller.changeGridSize(
                  (jsValue \ "length").as[Int],
                  (jsValue \ "width").as[Int]
                )
                completeWithData(controller.gridToJson.toString)
              case "changeGridStitch" =>
                controller.changeGridStitch(
                  StitchConverter.stringToStitch
                    .getOrElse(
                      ((jsValue \ "stitch").as[String]).toLowerCase,
                      Stitch.Square
                    )
                )
                completeWithData(controller.gridToJson.toString)
              case "fillGrid" =>
                controller.fillGrid(
                  rgbaToColor((jsValue \ "color").as[String])
                )
                completeWithData(controller.gridToJson.toString)
              case "bead" =>
                completeWithData(
                  controller
                    .beadToJson(
                      (jsValue \ "row").as[Int],
                      (jsValue \ "col").as[Int]
                    )
                    .toString
                )
              case "setBeadColor" =>
                controller.setBeadColor(
                  (jsValue \ "row").as[Int],
                  (jsValue \ "col").as[Int],
                  rgbaToColor((jsValue \ "color").as[String])
                )
                completeWithData(controller.gridToJson.toString)
              case _ => completeWithData(controller.gridToJson.toString)
            }
          }
        }
      }
    )

  private def completeWithData(data: String): StandardRoute = {
    complete(
      HttpEntity(
        ContentTypes.`application/json`,
        data
      )
    )
  }

  private def save: Try[Unit] = {
    val saveRequest = Http().singleRequest(
      HttpRequest(
        uri = s"$persistenceServiceEndpoint/save",
        method = HttpMethods.POST,
        entity = HttpEntity(
          ContentTypes.`application/json`,
          controller.gridToJson.toString
        )
      )
    )
    val responseJsonFuture = saveRequest.flatMap { response =>
      Unmarshal(response.entity).to[String].map { jsonString =>
        Json.parse(jsonString)
      }
    }
    Try {
      println(
        s"Sending request to $persistenceServiceEndpoint/save"
      )
      Await.result(responseJsonFuture, 10.seconds)
    }.map(_ => ())
  }

  def load(id: Int): Try[JsValue] = {
    val loadRequest = Http().singleRequest(
      HttpRequest(
        uri = s"$persistenceServiceEndpoint/load/$id",
        method = HttpMethods.GET
      )
    )
    val responseJsonFuture = loadRequest.flatMap { response =>
      Unmarshal(response.entity).to[String].map { jsonString =>
        Json.parse(jsonString)
      }
    }
    Try {
      println(
        s"Sending request to $persistenceServiceEndpoint/load"
      )
      Await.result(responseJsonFuture, 10.seconds)
    }
  }

  private def loadAll(): Try[Seq[JsValue]] = {
    val loadAllRequest = Http().singleRequest(
      HttpRequest(
        uri = s"$persistenceServiceEndpoint/loadAll",
        method = HttpMethods.GET
      )
    )
    val responseJsonFuture = loadAllRequest.flatMap { response =>
      Unmarshal(response.entity).to[String].map { jsonString =>
        Json.parse(jsonString).as[Seq[JsValue]]
      }
    }
    Try {
      println(
        s"Sending request to $persistenceServiceEndpoint/loadAll"
      )
      Await.result(responseJsonFuture, 10.seconds)
    }
  }

  def start(): Unit = {
    val binding = Http().newServerAt("0.0.0.0", 3000).bind(routes)

    binding.onComplete {
      case Success(serverBinding) =>
        println(
          "BeadsControllerAPI connected successfully at http://localhost:3000/"
        )
        complete(status = 200, serverBinding.toString())
      case Failure(exception) =>
        complete(status = 500, exception.getMessage)
    }
  }

  def stop(): Unit = {
    system.terminate()
  }

  object DB_UpdateObserver extends Observer {
    override def update(e: Event): Unit = {
      val updateRequest = Http().singleRequest(
        HttpRequest(
          uri = s"$persistenceServiceEndpoint/update",
          method = HttpMethods.POST,
          entity = HttpEntity(
            ContentTypes.`application/json`,
            controller.gridToJson.toString
          )
        )
      )
      val responseJsonFuture = updateRequest.flatMap { response =>
        Unmarshal(response.entity).to[String].map { jsonString =>
          Json.parse(jsonString)
        }
      }

      e match
        case Event.GRID =>
          Try {
            Await.result(responseJsonFuture, 10.seconds)
          }.map(_ => ())
    }
  }
}
