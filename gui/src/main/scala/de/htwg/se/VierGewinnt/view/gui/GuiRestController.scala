package de.htwg.se.VierGewinnt.view.gui

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.Http
import de.htwg.se.VierGewinnt.core.service.CoreRestService
import de.htwg.se.VierGewinnt.util.Observer
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

class GuiRestController {
  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "GuiAPI")

  given ActorSystem[Any] = system

  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext
  val coreServer = "http://localhost:8080/core"

  def getGridSize: Int =
    val future: Future[String] = sendGetRequest(coreServer + "/gridSize")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)
    result.toInt

  def getPlaygroundState: String =
    val future: Future[String] = sendGetRequest(coreServer + "/playgroundState")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)
    result

  def getGameState: String =
    val future: Future[String] = sendGetRequest(coreServer + "/gameState")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)
    result

  def getWinnerChips: Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] =
    val future: Future[String] = sendGetRequest(coreServer + "/winnerChips")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)
    None

  def getChipColor(row: Int, col: Int): String =
    val future: Future[String] = sendGetRequest(s"${coreServer}/chipColor/${row}/${col}")
    val result: String = Await.result(future, Duration.Inf)
    result

  def doAndPublishInsertChip(move: Int): Unit =
    val future: Future[String] = sendGetRequest(s"${coreServer}/doAndPublish/insChip/${move}")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def doAndPublishUndo: Unit =
    val future: Future[String] = sendGetRequest(s"${coreServer}/doAndPublish/undo")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def doAndPublishRedo: Unit =
    val future: Future[String] = sendGetRequest(s"${coreServer}/doAndPublish/redo")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def save: Unit =
    val future: Future[String] = sendGetRequest(s"${coreServer}/save")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def load: Unit =
    val future: Future[String] = sendGetRequest(s"${coreServer}/load")
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def setupGame(gameType: Int, size: Int): String =
    val future: Future[String] = sendGetRequest(s"${coreServer}/setupGame/${gameType}/${size}")
    val result: String = Await.result(future, Duration.Inf)
    result

  def addToObserver: Unit =
    val observerPayload = Json.obj(
      "name" -> "GUI",
      "serverAddress" -> "http://localhost:3000"
    )
    val future: Future[String] = sendPostRequest(s"${coreServer}/observer", Json.toJson("Json.stringify(Json.toJson(toObserve))").toString)
    val result: String = Await.result(future, Duration.Inf)
    logger.info(result)

  def sendGetRequest(url: String): Future[String] =
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = url
    )
    val response: Future[HttpResponse] = Http().singleRequest(request)
    handleResponse(response)

  def sendPostRequest(url: String, payload: String): Future[String] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      entity = HttpEntity(ContentTypes.`application/json`, payload)
    )
    val response: Future[HttpResponse] = Http().singleRequest(request)
    handleResponse(response)

  def handleResponse(responseFuture: Future[HttpResponse]): Future[String] =
    responseFuture.flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          Unmarshal(response.entity).to[String]
        case _ =>
          logger.error(s"Failed! Response status: ${response.status}")
          Future.failed(new RuntimeException(s"Failed! Response status: ${response.status}"))
      }
    }

}
