package de.htwg.se.beads_controller.controller.controllerComponent.controllerRestImpl

import de.htwg.se.beads_util.Enums.Stitch
import de.htwg.se.beads_util.Enums.ColorConverter
import de.htwg.se.beads_controller.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads_util.service.HttpService
import de.htwg.se.beads.model.gridComponent.GridInterface
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.unmarshalling.Unmarshal
import play.api.libs.json.*
import scala.concurrent.Await
import scala.concurrent.duration.*
import akka.http.javadsl.model.RequestEntity
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}
import scala.compiletime.uninitialized
import scalafx.scene.paint.Color
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.beads_util.Enums.StitchConverter.stringToStitch
import com.google.inject.Inject
import com.google.inject.Guice
import de.htwg.se.beads_controller.BeadsModule
import de.htwg.se.beads.model.fileIOComponent.FileIOInterface
import de.htwg.se.beads.model.gridComponent.BeadInterface
import de.htwg.se.beads.model.gridComponent.gridBaseImpl.Bead
import de.htwg.se.beads_util.util.Observable
import de.htwg.se.beads_util.Enums.Event
import org.checkerframework.checker.units.qual.s

class RestController @Inject() (
    httpService: HttpService = HttpService()
) extends Observable
    with ControllerInterface {

  val injector = Guice.createInjector(new BeadsModule)
  val fileIo = injector.getInstance(classOf[FileIOInterface])

  private val restControllerUrl = "http://localhost:3000/controller"

  var grid: GridInterface = injector.getInstance(classOf[GridInterface])
  gridRequest(restControllerUrl, "grid", HttpMethods.GET)
  var errorMsg: Option[String] = None

  def gridRequest(
      endpoint: String,
      command: String,
      method: HttpMethod,
      data: Option[JsValue] = None
  ): Unit = {

    val response =
      httpService
        .request(endpoint, command, method, data)
        .map(fileIo.jsonToGrid(_))

    response match {
      case Success(newGrid) =>
        grid = newGrid
        errorMsg = None
        notifyObservers(Event.GRID)
      case Failure(exception) =>
        errorMsg = Some(exception.getMessage)
        notifyObservers(Event.GRID)
    }

  }

  override def gridLength: Int = {
    httpService.request(
      restControllerUrl,
      "gridLength",
      HttpMethods.GET
    ) match {
      case Success(json) => json.as[Int]
      case Failure(_)    => grid.length
    }
  }

  override def gridWidth: Int = {
    httpService.request(
      restControllerUrl,
      "gridWidth",
      HttpMethods.GET
    ) match {
      case Success(json) => json.as[Int]
      case Failure(_)    => grid.width
    }
  }

  override def gridStitch: Stitch = {
    httpService.request(
      restControllerUrl,
      "gridStitch",
      HttpMethods.GET
    ) match {
      case Success(json) => stringToStitch(json.as[String].toLowerCase)
      case Failure(_)    => grid.stitch
    }
  }

  override def createEmptyGrid(
      length: Int,
      width: Int,
      stitch: Stitch
  ): Unit = {
    gridRequest(
      restControllerUrl,
      "createEmptyGrid",
      HttpMethods.POST,
      Some(
        Json.obj(
          "length" -> length,
          "width" -> width,
          "stitch" -> stitch.toString()
        )
      )
    )
  }

  override def bead(row: Int, col: Int): BeadInterface = {
    httpService.request(
      restControllerUrl,
      "bead",
      HttpMethods.POST,
      Some(
        Json.obj(
          "row" -> row,
          "col" -> col
        )
      )
    ) match {
      case Success(json) => fileIo.jsonToBead(json)
      case Failure(_)    => new Bead()
    }
  }

  override def beadToJson(row: Int, col: Int): JsValue = {
    httpService.request(
      restControllerUrl,
      "bead",
      HttpMethods.POST,
      Some(
        Json.obj(
          "row" -> row,
          "col" -> col
        )
      )
    ) match {
      case Success(json) => json
      case Failure(_)    => Json.obj()
    }
  }

  override def setBeadColor(row: Int, col: Int, color: Color): Unit = {
    gridRequest(
      restControllerUrl,
      "setBeadColor",
      HttpMethods.POST,
      Some(
        Json.obj(
          "row" -> row,
          "col" -> col,
          "color" -> ColorConverter.colorToRGBA(color)
        )
      )
    )
  }

  override def changeGridSize(length: Int, width: Int): Unit = {
    gridRequest(
      restControllerUrl,
      "changeGridSize",
      HttpMethods.POST,
      Some(
        Json.obj(
          "length" -> length,
          "width" -> width
        )
      )
    )
  }

  override def changeGridStitch(stitch: Stitch): Unit = {
    gridRequest(
      restControllerUrl,
      "changeGridStitch",
      HttpMethods.POST,
      Some(
        Json.obj(
          "stitch" -> stitch.toString()
        )
      )
    )
  }

  override def fillGrid(color: Color): Unit = {
    gridRequest(
      restControllerUrl,
      "fillGrid",
      HttpMethods.POST,
      Some(
        Json.obj(
          "color" -> ColorConverter.colorToRGBA(color)
        )
      )
    )
  }

  override def gridToString: String = {
    httpService.request(
      restControllerUrl,
      "gridString",
      HttpMethods.GET
    ) match {
      case Success(json) => json.as[String]
      case Failure(_)    => grid.toString
    }
  }

  override def gridToJson: JsValue = {
    httpService.request(restControllerUrl, "grid", HttpMethods.GET) match {
      case Success(json) => json
      case Failure(_)    => Json.obj()
    }
  }

  override def undo(): Unit = {
    gridRequest(restControllerUrl, "undo", HttpMethods.GET)
    notifyObservers(Event.GRID)
  }

  override def redo(): Unit = {
    gridRequest(restControllerUrl, "redo", HttpMethods.GET)
    notifyObservers(Event.GRID)
  }

  override def save(): Unit = {
    httpService.request(
      restControllerUrl,
      "save",
      HttpMethods.GET
    )
    notifyObservers(Event.GRID)
  }

  override def load(): Unit = {

    gridRequest(restControllerUrl, "load", HttpMethods.GET)
    notifyObservers(Event.GRID)
  }
}
