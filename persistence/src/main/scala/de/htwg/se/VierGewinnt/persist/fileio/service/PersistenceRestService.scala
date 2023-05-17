package de.htwg.se.VierGewinnt.persist.fileio

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import org.slf4j.LoggerFactory
import scala.concurrent._
import scala.concurrent.ExecutionContextExecutor
import scala.util.Failure
import scala.util.Success

object PersistenceRestService extends App {
  // Create the logger
  private val logger = LoggerFactory.getLogger(getClass)

  // needed to run the route
  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "PersistenceService")
  given ActorSystem[Any] = system

  // needed for the future flatMap/onComplete in the end
  val executionContext: ExecutionContextExecutor = system.executionContext
  given ExecutionContextExecutor = executionContext

  val routes: String =
    """
    Welcome to the Persistence REST service! Available routes:
    GET   /fileio/load
    POST  /fileio/save
    """.stripMargin

  val route = concat(
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, routes))
    },
    path("fileio" / "load") {
      get {
        logger.info("Received load request: {}", PersistenceController.load())
        complete(HttpEntity(ContentTypes.`application/json`, PersistenceController.load()))
      }
    },
    path("fileio" / "save") {
      concat(
        post {
          entity(as[String]) { game =>
            logger.info("Received save request with game: {}", game)
            PersistenceController.save(game)
            complete("game saved")
          }
        }
      )
    }
  )

  val bindingFuture = Http().newServerAt("localhost", 8081).bind(route)

  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"File IO REST service online at http://localhost:${address.getPort}")

    case Failure(exception) =>
      logger.error(s"File IO REST service couldn't be started! Error: {}", exception.getMessage)
  }
}
