package de.htwg.se.beads_persistence.database.mongodb

import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

import de.htwg.se.beads_persistence.database.DAO_Interface
import de.htwg.se.beads.model.fileIOComponent.fileIoJsonImpl.FileIO
import de.htwg.se.beads.model.gridComponent.GridInterface

import scala.concurrent.duration._
import scala.util.Try
import scala.concurrent.Await
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.util.Failure
import scala.util.Success

object BeadsMongoDB extends DAO_Interface {

  private val connectionRetryAttempts = 5
  private val maxWaitSeconds = 30.seconds

  private val fileIO = new FileIO

  private val client: MongoClient = MongoClient(
    "mongodb://root:root@mongodb:27017"
  )
  private val db: MongoDatabase = client.getDatabase("mongodb")

  private val collectionName = "grids"
  private val collection: MongoCollection[Document] =
    db.getCollection(collectionName)

  init()

  private def init(): Unit = {
    println("Attempting to connect to MongoDB...")
    val pingResult = db.runCommand(Document("ping" -> 1)).toFuture()
    Try(Await.result(pingResult, maxWaitSeconds)) match {
      case Success(_) => println("MongoDB connection established")
      case Failure(e) => println(s"MongoDB connection failed: ${e.getMessage}")
    }
  }

  private var idCounter: Int = 0

  override def save(grid: GridInterface): Try[Unit] = {
    idCounter += 1
    val document = Document(
      "_id" -> idCounter,
      "gridData" -> fileIO.gridToJson(grid).toString
    )

    Try[Unit] {
      Await.result(collection.insertOne(document).toFuture(), maxWaitSeconds)
    }
  }

  override def load(id: Int): Try[JsValue] = {
    val query = collection.find(equal("_id", id)).first().headOption()

    Try(Await.result(query, maxWaitSeconds)).flatMap {
      case Some(doc) =>
        Success(Json.parse(doc.getString("gridData")))
      case None =>
        Failure(new Exception("No grid found"))
    }
  }

  override def loadAll(): Try[Seq[(Int, JsValue)]] = {
    val query = collection.find().toFuture()

    Try(Await.result(query, maxWaitSeconds)).map { docs =>
      docs.map { doc =>
        (doc.getInteger("_id"), Json.parse(doc.getString("gridData")))
      }
    }
  }

  override def update(id: Int, grid: GridInterface): Try[JsValue] = {
    val updatedDocument = Document(
      "_id" -> id,
      "gridData" -> fileIO.gridToJson(grid).toString
    )

    val query = collection
      .findOneAndReplace(equal("_id", id), updatedDocument)
      .headOption()

    Try(Await.result(query, maxWaitSeconds)).flatMap {
      case Some(_) => Success(fileIO.gridToJson(grid))
      case None    => Failure(new Exception("No grid found"))
    }
  }

  override def delete(id: Int): Try[Unit] = {
    val query = collection.deleteOne(equal("_id", id)).toFuture()

    Try(Await.result(query, maxWaitSeconds)).map(_ => ())
  }
}
