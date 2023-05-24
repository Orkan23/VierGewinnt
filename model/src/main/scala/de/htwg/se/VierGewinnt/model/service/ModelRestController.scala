package de.htwg.se.VierGewinnt.model.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.{Cell, Chip, Grid}
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.{PlaygroundPvE, PlaygroundPvP}
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.{HumanPlayer, BotPlayer}
import de.htwg.se.VierGewinnt.model.service.ModelRestService
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class ModelRestController {
  private val logger = LoggerFactory.getLogger(ModelRestService.getClass)

  val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreAPI")

  given ActorSystem[Any] = system

  val executionContext: ExecutionContextExecutor = system.executionContext

  given ExecutionContextExecutor = executionContext

  def sendGetRequest(url: String): Future[HttpResponse] = {
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = url
    )

    Http().singleRequest(request)
  }

  def sendPostRequest(url: String, payload: String): Future[HttpResponse] = {
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      entity = HttpEntity(ContentTypes.`application/json`, payload)
    )

    Http().singleRequest(request)
  }

  def handleResponse(responseFuture: Future[HttpResponse], onSuccess: String => Unit): Unit =
    responseFuture
      .onComplete {
        case Failure(_) =>
          logger.error("Failed getting JSON")
        case Success(response) if response.status == StatusCodes.OK =>
          Unmarshal(response.entity).to[String].onComplete {
            case Failure(_) =>
              logger.error("Failed unmarshalling")
            case Success(jsonString) =>
              onSuccess(jsonString)
          }
        case Success(response) =>
          logger.error(s"Unexpected response status: ${response.status}")
      }

  def jsonToPlayground(source: String): PlaygroundInterface =
    val json: JsValue = Json.parse(source)

    val size = (json \ "playground" \ "size").get.toString.toInt
    val gameType = (json \ "playground" \ "gameType").get.toString.toInt
    val player1 = (json \ "playground" \ "player1").get.toString().replaceAll("\"", "")
    val player2 = (json \ "playground" \ "player2").get.toString().replaceAll("\"", "")

    var grid: GridInterface = new Grid(size)

    for (index <- 0 until size * size)
      val row = (json \\ "row") (index).as[Int]
      val col = (json \\ "col") (index).as[Int]
      val chip = (json \\ "chip") (index).as[String]

      val _grid = chip match
        case "EMPTY" => grid.replaceCell(row, col, Cell(Chip.EMPTY))
        case "RED" => grid.replaceCell(row, col, Cell(Chip.RED))
        case "YELLOW" => grid.replaceCell(row, col, Cell(Chip.YELLOW))

      _grid match {
        case Success(value) => grid = value
        case Failure(e) =>
      }

    val pl1: (String, Chip) = getNameAndChip(player1)
    val pl2: (String, Chip) = getNameAndChip(player2)

    val humanPlayer1 = HumanPlayer(pl1._1, pl1._2)  // create or get with slick
    val humanPlayer2 = HumanPlayer(pl2._1, pl2._2) // create or get with slick
    if (gameType == 0) then PlaygroundPvP(grid, List(humanPlayer1, humanPlayer2))
    else PlaygroundPvE(grid, List(HumanPlayer(pl1._1, pl1._2), BotPlayer(pl2._1, pl2._2)))

  def getNameAndChip(playerName: String): (String, Chip) =
    (playerName.split("&")(0), if (playerName.split("&")(1)) == "RED" then Chip.RED else Chip.YELLOW)

}
