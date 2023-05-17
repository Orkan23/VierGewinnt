package de.htwg.se.VierGewinnt.core.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.Http
import com.google.inject.Guice
import com.google.inject.Injector
import de.htwg.se.VierGewinnt.core.*
import de.htwg.se.VierGewinnt.util.Move
import de.htwg.se.VierGewinnt.util.Observer
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import scala.concurrent.ExecutionContextExecutor
import scala.util.Failure
import scala.util.Success
import spray.json.enrichAny
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol.IntJsonFormat
import spray.json.JsValue
import spray.json.RootJsonFormat

object CoreRestService extends App {
  val injector: Injector = Guice.createInjector(CoreModule())
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreAPI")

  given ActorSystem[Any] = system

  val executionContext = system.executionContext

  given ExecutionContextExecutor = executionContext

  val servicePort = 8080

  val route =
    concat(
      get {
        path("core" / "load") {
          controller.load
          complete(HttpEntity(ContentTypes.`application/json`, "loaded"))
        }
      },
      post {
        path("core" / "save") {
          controller.save
          complete(HttpEntity(ContentTypes.`application/json`, "saved"))
        }
      },
      get {
        path("core" / "setupGame" / IntNumber / IntNumber) { (gameType, size) =>
          controller.setupGame(gameType, size)
          complete(HttpEntity(ContentTypes.`application/json`, "game set up"))
        }
      },
      post {
        path("core" / "insChip") {
          entity(as[String]) { request =>
            val json = Json.parse(request)
            val move = (json \ "size").get.toString.replaceAll("\"", "").toInt
            val updatedPlayground = controller.insChip(Move(move))
            complete(HttpEntity(ContentTypes.`application/json`, Util.toJson(updatedPlayground).toString))
          }
        }
      },
      get {
        path("core" / "undo") {
          val updatedPlayground = controller.undo
          complete(HttpEntity(ContentTypes.`application/json`, Util.toJson(updatedPlayground).toString))
        }
      },
      get {
        path("core" / "redo") {
          val updatedPlayground = controller.redo
          complete(HttpEntity(ContentTypes.`application/json`, Util.toJson(updatedPlayground).toString))
        }
      },
      get {
        path("core" / "playgroundState") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.playgroundState).toString))
        }
      },
      get {
        path("core" / "gameState") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.printState).toString))
        }
      },
      get {
        path("core" / "winnerChips") {
          complete(
            HttpEntity(
              ContentTypes.`application/json`,
              controller.winnerChips match {
                case None    => "None"
                case Some(v) => Json.toJson(v).toString
              }
            )
          )
        }
      },
      post {
        path("core" / "restartGame") {
          controller.restartGame
          complete(HttpEntity(ContentTypes.`application/json`, "game restarted"))
        }
      },
      get {
        path("core" / "isPreparing") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.isPreparing).toString))
        }
      },
      get {
        path("core" / "gameNotDone") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.gameNotDone).toString))
        }
      },
      get {
        path("core" / "chipColor" / IntNumber / IntNumber) { (row, col) =>
          complete(HttpEntity(ContentTypes.`application/json`, controller.getChipColor(row, col)))
        }
      },
      get {
        path("core" / "toString") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.toString).toString))
        }
      },
      get {
        path("core" / "doAndPublish" / "insChip" / IntNumber) { i =>
          controller.doAndPublish(controller.insChip, Move(i))
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("do and publish " + i).toString))
        }
      },
      get {
        path("core" / "doAndPublish" / "undo") {
          controller.doAndPublish(controller.undo)
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("undo").toString))
        }
      },
      get {
        path("core" / "doAndPublish" / "redo") {
          controller.doAndPublish(controller.redo)
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("redo").toString))
        }
      },
      get {
        path("core" / "gridSize") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(controller.gridSize).toString))
        }
      },
      post {
        path("core" / "addObserver") {
          entity(as[String]) { request =>
            val json = Json.parse(request)
            print(json)
            complete(HttpEntity(ContentTypes.`application/json`, "Observer added"))
          }
        }
      }
    )
  val bindingFuture = Http().newServerAt("localhost", servicePort).bind(route)
  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"Core REST service online at http://localhost:${address.getPort}")

    case Failure(exception) =>
      logger.error(s"Core REST service couldn't be started! Error: {}", exception.getMessage)
  }

}
