package de.htwg.se.VierGewinnt.core.service

import akka.actor.typed.scaladsl.AskPattern.*
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
import concurrent.duration.DurationInt
import de.htwg.se.VierGewinnt.core.service.CoreRestService
import de.htwg.se.VierGewinnt.core.Util
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import spray.json.RootJsonFormat

class CoreRestController {
  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreAPI")

  given ActorSystem[Any] = system

  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext

  val utilServer = "http://localhost:8083"

  def notifyObservers:Unit =
    sendGetRequest(s"${utilServer}/observer/notifyObservers")


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
          logger.warn(s"Failed! Response status: ${response.status}")
          Future.failed(new RuntimeException(s"Failed! Response status: ${response.status}"))
      }
    }
}
