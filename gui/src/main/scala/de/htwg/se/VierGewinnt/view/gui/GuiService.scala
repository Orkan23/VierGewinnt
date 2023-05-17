package de.htwg.se.VierGewinnt.view.gui

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.Http
import com.google.inject.Guice
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import scala.concurrent.ExecutionContextExecutor
import scala.util.Failure
import scala.util.Success
import scala.util.Try

object GuiService extends App {

  val injector = Guice.createInjector(new GuiModule())
  val gui = injector.getInstance(classOf[GUI])

  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "GUI")

  given ActorSystem[Any] = system

  val executionContext = system.executionContext

  given ExecutionContextExecutor = executionContext

  val servicePort = 3000

  val route =
    concat(
      get {
        path("update") {
          gui.update
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("GUI updated").toString))
        }
      }
    )

  val bindingFuture = Http().newServerAt("localhost", servicePort).bind(route)
  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"GUI REST service online at http://localhost:${address.getPort}")

    case Failure(exception) =>
      logger.error(s"GUI REST service couldn't be started! Error: {}", exception.getMessage)
  }

  new Thread {
    override def run(): Unit = gui.main(Array())
  }.start()

}
