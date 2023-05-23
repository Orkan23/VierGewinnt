package de.htwg.se.VierGewinnt.model.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.server.Directives.*
import com.google.inject.{Guice, Injector}
import de.htwg.se.VierGewinnt.model.{ModelModule, Util}
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import spray.json.DefaultJsonProtocol.IntJsonFormat
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat, enrichAny}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn.readLine
import scala.util.{Failure, Success}

object ModelRestService extends App {

  val injector: Injector = Guice.createInjector(new ModelModule)

  val playgroundPvP: PlaygroundInterface = injector.getInstance(classOf[PlaygroundInterface])

  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "ModelAPI")

  given ActorSystem[Any] = system

  val executionContext = system.executionContext

  given ExecutionContextExecutor = executionContext

  val servicePort = 8082

  val restController = new ModelRestController

  val route =
    concat(
      path("playground") {
        get {
          logger.info(s"Received playground request ${Util.playgroundToJson(playgroundPvP).toString}")
          complete(HttpEntity(ContentTypes.`application/json`, Util.playgroundToJson(playgroundPvP).toString))
        }
      },
    )
  val bindingFuture = Http().newServerAt("0.0.0.0", servicePort).bind(route)
  //readLine
  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"Model REST service online at http://0.0.0.0:${servicePort}")

    case Failure(exception) =>
      logger.error(s"Model REST service couldn't be started! Error: {}", exception.getMessage)
  }

}
