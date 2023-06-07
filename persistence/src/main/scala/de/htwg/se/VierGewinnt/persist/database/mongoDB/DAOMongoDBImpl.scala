package de.htwg.se.VierGewinnt.persist.database.mongoDB

import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import java.util.Random
import org.mongodb.scala.*
import org.mongodb.scala.model.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Projections.*
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.model.Updates.*
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.result.UpdateResult
import org.mongodb.scala.Document
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.concurrent.duration.Duration
import scala.concurrent.Await

class DAOMongoDBImpl extends DAOInterface {

  val database_username = sys.env.getOrElse("MONGO_INITDB_ROOT_USERNAME", "root")
  val database_pw = sys.env.getOrElse("MONGO_INITDB_ROOT_PASSWORD", "mongo")
  val databaseUrl: String = s"mongodb://$database_username:$database_pw@mongoDB/"

  val client: MongoClient = MongoClient(databaseUrl)
  val db: MongoDatabase = client.getDatabase("VierGewinnt")
  val playgroundCollection: MongoCollection[Document] = db.getCollection("playground")

  override def create: Unit = {
    val playgroundDocument: Document = Document("_id" -> "playgroundDocument")
    observerInsertion(playgroundCollection.insertOne(playgroundDocument))
  }

  override def read: String = {
    val documents = playgroundCollection.find().toFuture()
    val result = Await.result(documents, Duration.Inf)
    result.map(_.toJson()).mkString("\n")
  }

  override def update(input: String): Unit = {
    val updateObservable = playgroundCollection.updateOne(equal("_id", "playgroundDocument"), set("playground", input))

    observerUpdate(updateObservable)
  }

  override def delete: Unit = {
    playgroundCollection.deleteOne(equal("_id", "playgroundDocument"))
  }

  private def observerInsertion(insertObservable: SingleObservable[InsertOneResult]): Unit = {
    insertObservable.subscribe(new Observer[InsertOneResult] {
      override def onNext(result: InsertOneResult): Unit = println(s"inserted: $result")

      override def onError(e: Throwable): Unit = println(s"insert onError: $e")

      override def onComplete(): Unit = println("completed insert")
    })
  }

  private def observerUpdate(insertObservable: SingleObservable[UpdateResult]): Unit = {
    insertObservable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(s"updated: $result")

      override def onError(e: Throwable): Unit = println(s"update onError: $e")

      override def onComplete(): Unit = println("completed update")
    })
  }
}
