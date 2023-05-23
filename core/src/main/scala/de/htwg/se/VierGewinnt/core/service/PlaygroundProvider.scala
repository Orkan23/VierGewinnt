package de.htwg.se.VierGewinnt.core.service

import akka.http.scaladsl.model.HttpResponse
import com.google.inject.Inject
import com.google.inject.Provider
import de.htwg.se.VierGewinnt.core.Util
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import org.slf4j.LoggerFactory
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future

class PlaygroundProvider @Inject() (restController: CoreRestController) extends Provider[PlaygroundInterface] {
  val modelServer = "http://0.0.0.0:8082"

  private val logger = LoggerFactory.getLogger(getClass)

  override def get(): PlaygroundInterface = {
    val playgroundFuture : Future[String] = restController.sendGetRequest(modelServer + "/playground")

    val playgroundResult: String = Await.result(playgroundFuture, Duration.Inf)
    logger.info(playgroundResult)
    Util.jsonToPlayground(playgroundResult)
  }
}
