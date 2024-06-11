package de.htwg.se.beads_util.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.HttpMethod
import play.api.libs.json.JsValue
import scala.util.Try
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import play.api.libs.json.Json
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class HttpService {

  implicit val system: ActorSystem[?] =
    ActorSystem(Behaviors.empty, "SingleRequest")
  implicit val executionContext: ExecutionContext = system.executionContext

  def request(
      endpoint: String,
      command: String,
      method: HttpMethod,
      data: Option[JsValue] = None
  ): Try[JsValue] = {

    val responseFuture = Http().singleRequest(
      HttpRequest(
        method = method,
        uri = s"$endpoint/$command",
        entity = data match {
          case Some(data) =>
            HttpEntity(ContentTypes.`application/json`, Json.stringify(data))
          case None =>
            HttpEntity.Empty
        }
      )
    )

    val responseJsonFuture = responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String].map { jsonString =>
        Json.parse(jsonString)
      }
    }

    Try {
      Await.result(responseJsonFuture, 10.seconds)
    }
  }
}
