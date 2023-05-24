package de.htwg.se.VierGewinnt.persist.fileio.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.stream.scaladsl.Source
import com.google.inject.Guice
import com.google.inject.Injector
import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import de.htwg.se.VierGewinnt.persist.fileio.FileIOInterface
import de.htwg.se.VierGewinnt.persist.fileio.PersistenceModule
import org.slf4j.LoggerFactory
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.concurrent.*
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object PersistenceRestService extends App {
  val injector: Injector = Guice.createInjector(PersistenceModule())
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  val dao: DAOInterface = injector.getInstance(classOf[DAOInterface])

  private val logger = LoggerFactory.getLogger(getClass)

  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "PersistenceService")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val routes: String =
    """
      Welcome to the Persistence REST service! Available routes:
      GET   /fileio/load
      POST  /fileio/save
      """.stripMargin

  val route = concat(
    get {
      path("fileio" / "json" / "load") {
        val result = fileIo.load match
          case Success(payload) => payload
          case Failure(_)       => Json.toJson("Could not load the game").toString
        logger.info(s"Received load request: ${result}")
        complete(HttpEntity(ContentTypes.`application/json`, result))
      }
    },
    get {
      path("fileio" / "json" / "hello") {
        logger.info("Hello")
        complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("Hello").toString))
      }
    },
    post {
      path("fileio" / "json" / "save") {
        entity(as[String]) { game =>
          logger.info(s"Received save request with game: ${game.toString.replaceAll("\n","")}")
          fileIo.save(game)
          dao.update(game)
          complete("game saved")
        }
      }
    },
    get {
      path("db" / "create") {
        dao.create()
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Database created"))
      }
    },
    get {
      path("db" / "read") {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, dao.read()))
      }
    },
    post {
      path("db" / "update") {
        entity(as[String]) { game =>
          dao.update(game)
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "db update received"))
        }
      }
    },
    get {
      path("db" / "delete") {
        dao.delete()
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "db tables has been deleted"))
      }
    }
  )

  val bindingFuture = Http().newServerAt("0.0.0.0", 8081).bind(route)

  println(s"File IO REST service online at http://0.0.0.0:8081/fileio/json/hello")
}
