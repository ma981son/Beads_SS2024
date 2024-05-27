package de.htwg.se.beads_controller.controller.controllerComponent.controllerRestImpl

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

class BeadsControllerAPI(using controller: ControllerInterface) {

  implicit val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "BeadsControllerAPI")
  implicit val executionContext: ExecutionContext = system.executionContext

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
            case "grid" => completeWithData(controller.gridToJson.toString)
            case "gridString" => completeWithData(controller.gridToString)
            case "gridLength" =>
              completeWithData(controller.gridLength.toString())
            case "gridWidth" =>
              completeWithData(controller.gridWidth.toString())
            case "gridStitch" =>
              completeWithData(controller.gridStitch.toString())
            case "undo" =>
              controller.undo()
              completeWithData(controller.gridToJson.toString)
            case "redo" =>
              controller.redo()
              completeWithData(controller.gridToJson.toString)
            case "save" =>
              controller.save()
              completeWithData(controller.gridToJson.toString)
            case "load" =>
              controller.load()
              completeWithData(controller.gridToJson.toString)
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

  def start(): Unit = {
    val binding = Http().newServerAt("localhost", 3000).bind(routes)

    binding.onComplete {
      case Success(serverBinding) =>
        complete(status = 200, serverBinding.toString())
      case Failure(exception) =>
        complete(status = 500, exception.getMessage)
    }
  }

  def stop(): Unit = {
    system.terminate()
  }
}
