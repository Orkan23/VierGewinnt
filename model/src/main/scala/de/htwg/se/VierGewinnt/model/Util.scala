package de.htwg.se.VierGewinnt.model

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.BotPlayer
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.HumanPlayer
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.io.Source
import scala.util.Failure
import scala.util.Success
import scala.xml.Elem
import scala.xml.XML

object Util {

  def toJsonString(pg: PlaygroundInterface): String =
    Json.prettyPrint(playgroundToJson(pg))

  def playgroundToJson(pg: PlaygroundInterface): JsValue = Json.obj(
    "playground" -> Json.obj(
      "size" -> JsNumber(pg.size),
      "gameType" -> JsNumber(if pg.isInstanceOf[PlaygroundPvP] then 0 else 1),
      "player1" -> JsString(pg.player(0).getName() + "&" + pg.player(0).getChip()),
      "player2" -> JsString(pg.player(1).getName() + "&" + pg.player(1).getChip()),
      "cells" -> Json.toJson(
        for {
          row <- 0 until pg.size
          col <- 0 until pg.size
        } yield {
          Json.obj(
            "row" -> row,
            "col" -> col,
            "chip" -> pg.grid.getCell(row, col).chip.toString
          )
        }
      )
    )
  )

  def jsonToPlayground(source: String): PlaygroundInterface =
    val json: JsValue = Json.parse(source)

    val size = (json \ "playground" \ "size").get.toString.toInt
    val gameType = (json \ "playground" \ "gameType").get.toString.toInt
    val player1 = (json \ "playground" \ "player1").get.toString().replaceAll("\"", "")
    val player2 = (json \ "playground" \ "player2").get.toString().replaceAll("\"", "")

    var grid: GridInterface = new Grid(size)

    for (index <- 0 until size * size)
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val chip = (json \\ "chip")(index).as[String]

      val _grid = chip match
        case "EMPTY"  => grid.replaceCell(row, col, Cell(Chip.EMPTY))
        case "RED"    => grid.replaceCell(row, col, Cell(Chip.RED))
        case "YELLOW" => grid.replaceCell(row, col, Cell(Chip.YELLOW))

      _grid match {
        case Success(value) => grid = value
        case Failure(e)     =>
      }

    val pl1: (String, Chip) = getNameAndChip(player1)
    val pl2: (String, Chip) = getNameAndChip(player2)

    if (gameType == 0) then PlaygroundPvP(grid, List(HumanPlayer(pl1._1, pl1._2), HumanPlayer(pl2._1, pl2._2)))
    else PlaygroundPvE(grid, List(HumanPlayer(pl1._1, pl1._2), BotPlayer(pl2._1, pl2._2)))

  def xmlToPlayground(source: String): PlaygroundInterface =
    val file = XML.loadString(source)
    val size = (file \\ "playground" \ "@size").text
    val gameType = (file \\ "playground" \ "@gameType").text
    val player1 = (file \\ "playground" \ "@player1").text
    val player2 = (file \\ "playground" \ "@player2").text

    var grid: GridInterface = new Grid(size.toInt)

    val cellNodes = file \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \\ "@row").text.toInt
      val col: Int = (cell \\ "@col").text.toInt

      var _grid = (cell \\ "@chip").text match
        case "EMPTY"  => grid.replaceCell(row, col, Cell(Chip.EMPTY))
        case "RED"    => grid.replaceCell(row, col, Cell(Chip.RED))
        case "YELLOW" => grid.replaceCell(row, col, Cell(Chip.YELLOW))

      _grid match
        case Success(g) => grid = g
        case Failure(e) =>
    }

    val pl1: (String, Chip) = getNameAndChip(player1)
    val pl2: (String, Chip) = getNameAndChip(player2)

    if (gameType == "0") then PlaygroundPvP(grid, List(HumanPlayer(pl1._1, pl1._2), HumanPlayer(pl2._1, pl2._2)))
    else PlaygroundPvE(grid, List(HumanPlayer(pl1._1, pl1._2), BotPlayer(pl2._1, pl2._2)))

  def playgroundToXml(pg: PlaygroundInterface): Elem =
    <playground size={pg.size.toString} gameType={if pg.isInstanceOf[PlaygroundPvP] then "0" else "1"} player1={
      pg.player(0).getName() + "&" + pg.player(0).getChip()
    } player2={pg.player(1).getName() + "&" + pg.player(1).getChip()}>
      {gridToXmL(pg.grid)}
    </playground>

  def gridToXmL(grid: GridInterface): Seq[Elem] =
    for {
      row <- 0 until grid.size
      col <- 0 until grid.size
    } yield {
      <cell row={row.toString} col={col.toString} chip={grid.getCell(row, col).chip.toString}>
      </cell>
    }

  def getNameAndChip(playerName: String): (String, Chip) =
    (playerName.split("&")(0), if (playerName.split("&")(1)) == "RED" then Chip.RED else Chip.YELLOW)

}
