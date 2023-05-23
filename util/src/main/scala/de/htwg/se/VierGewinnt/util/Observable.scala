/** Observable class for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */
package de.htwg.se.VierGewinnt.util

import com.google.inject.Guice
import com.google.inject.Injector
import de.htwg.se.VierGewinnt.util.service.UtilRestController
import org.slf4j.LoggerFactory
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future

trait Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit
  def remove(s: Observer): Unit
  def notifyObservers: Unit
}

class ObservableImpl extends Observable {
  override def add(s: Observer): Unit = subscribers = subscribers :+ s

  override def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

  override def notifyObservers: Unit = subscribers.foreach(o => o.update)
}

case class GuiObserver(address: String) extends Observer:
  private val logger = LoggerFactory.getLogger(getClass)
  val injector: Injector = Guice.createInjector(UtilModule())
  val restController: UtilRestController = injector.getInstance(classOf[UtilRestController])

  override def update: Unit =
    val futureResponse: Future[String] = restController.sendGetRequest(s"${address}/update")
    val response: String = Await.result(futureResponse, Duration.Inf)
    logger.info(response)
