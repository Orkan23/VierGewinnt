package de.htwg.se.VierGewinnt.persist.database.mongoDB

import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import org.mongodb.scala._
import org.mongodb.scala.model._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.model.Projections._
import java.util.Random

class DAOMongoDBImpl extends DAOInterface {

  private val databaseUrl: String = "mongodb://localhost:27017"

  val client: Option[MongoClient] = Some(MongoClient(databaseUrl))
  val db: MongoDatabase = client.getDatabase("vierGewinnt")
  val collection: MongoCollection[Document] = db.getCollection("game")
  override def create(json: String): Int = {
    val rand: Random = new Random()
    val id = rand.nextInt(900000)
  val document: Document = Document("_id" -> id, "game" -> json)
  val query = collection.insertOne(document)
  id
  }

  override def read(): String = {
    collection.find().printResults()
  }

  override def update(input: String): Unit ={
  val json: JsValue = Json.parse(input)
  collection.insertOne(Document(json))
  }

  override def delete(id: Int): Unit = {
    collection.deleteOne(equal("id", id))
  }
}
