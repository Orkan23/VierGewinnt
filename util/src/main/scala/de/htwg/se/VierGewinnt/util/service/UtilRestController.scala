package de.htwg.se.VierGewinnt.util.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}

class UtilRestController {
  private val logger = LoggerFactory.getLogger(getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "UtilAPI")

  given ActorSystem[Any] = system

  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext


  def sendGetRequest(url: String): Future[String] = {
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = url
    )
    val response: Future[HttpResponse] = Http().singleRequest(request)
    handleResponse(response)
  }

  def sendPostRequest(url: String, payload: String): Future[String] = {
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      entity = HttpEntity(ContentTypes.`application/json`, payload)
    )
    val response: Future[HttpResponse] = Http().singleRequest(request)
    handleResponse(response)
  }

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
