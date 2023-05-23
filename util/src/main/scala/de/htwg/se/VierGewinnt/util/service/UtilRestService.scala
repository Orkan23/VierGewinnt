package de.htwg.se.VierGewinnt.util.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.Http
import com.google.inject.Guice
import com.google.inject.Injector
import de.htwg.se.VierGewinnt.util.GuiObserver
import de.htwg.se.VierGewinnt.util.Move
import de.htwg.se.VierGewinnt.util.Observable
import de.htwg.se.VierGewinnt.util.ObservableImpl
import de.htwg.se.VierGewinnt.util.UtilModule
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object UtilRestService extends App {

  val injector: Injector = Guice.createInjector(UtilModule())
  val observable: ObservableImpl = injector.getInstance(classOf[ObservableImpl])

  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "UtilAPI")

  given ActorSystem[Any] = system

  val executionContext = system.executionContext

  given ExecutionContextExecutor = executionContext

  val servicePort = 8083

  val route =
    concat(
      post {
        path("observer" / "add") {
          entity(as[String]) { payload =>
            val observerJson = Json.parse(payload)
            val name: String = (observerJson \ "name").as[String]
            val serverAddress: String = "http://host.docker.internal:3000" //(observerJson \ "serverAddress").as[String]

            logger.info(payload)
            val observer = if (name == "gui") GuiObserver(serverAddress) else GuiObserver(serverAddress)
            if (observable.subscribers.contains(observer))
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(s"${observer} already exists").toString))
            else
              observable.add(observer)
              //observer.update
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(s"${name} is added to observer list").toString))

          }
        }
      },
      post {
        path("observer" / "remove") {
          entity(as[String]) { request =>
            val observer = Json.parse(request)

            complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("loaded").toString))
          }
        }
      },
      get {
        path("observer" / "notifyObservers") {
          observable.notifyObservers
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("notified all observers").toString))
        }
      },
      get {
        path("hello") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("Hello").toString))
        }
      }
    )

  val bindingFuture = Http().newServerAt("0.0.0.0", servicePort).bind(route)
  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"Util REST service online at http://0.0.0.0:${address.getPort}")

    case Failure(exception) =>
      logger.error(s"Util REST service couldn't be started! Error: {}", exception.getMessage)
  }
}
