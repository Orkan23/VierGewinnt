package de.htwg.se.VierGewinnt.persist.database.mongoDB

import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import org.mongodb.scala.*

class DAOMongoDBImpl extends DAOInterface {

  private val databaseUrl: String = "mongodb://localhost:27017"

  val client: Option[MongoClient] = Some(MongoClient(databaseUrl))
  val db: MongoDatabase = client.getDatabase("vierGewinnt")
  val collection: MongoCollection[Document] = db.getCollection("game")
  override def create(): Unit = None
  // daoPlayground.create()

  override def read(): String = ""
  val res: MongoCollection[Document] = db.getCollection("test")
  // daoPlayground.read()

  override def update(input: String): Unit = None
  // daoPlayground.update()

  override def delete(): Unit = None
  // daoPlayground.delete()
}
