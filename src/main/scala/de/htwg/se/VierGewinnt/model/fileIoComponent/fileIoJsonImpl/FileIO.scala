/** FileIO JSON Implementation for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */
package de.htwg.se.VierGewinnt.model.fileIoComponent.fileIoJsonImpl

import de.htwg.se.VierGewinnt.model.fileIoComponent.FileIOInterface
import de.htwg.se.VierGewinnt.model.fileIoComponent.Util.getNameAndChip
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.BotPlayer
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.HumanPlayer
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

import java.io.PrintWriter
import play.api.libs.json.*

import scala.io.Source
import scala.util.Failure
import scala.util.Success

/** FileIO JSON Implementation, to save and load the state of a game with a JSON file. */
class FileIO extends FileIOInterface {

  /** Load the game from a "playground.json" file and return the playground. */
  override def load: PlaygroundInterface =
    val source: String = Source.fromFile("playground.json").getLines().mkString
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

  
  /** Save the game to a "playground.json" file.
    *
    * @param playground
    *   The playground to save.
    */
  override def save(playground: PlaygroundInterface): Unit =
    import java.io._
    val pw = new PrintWriter(new File("playground.json"))
    pw.write(Json.prettyPrint(pgToJson(playground)))
    pw.close()

    def pgToJson(pg: PlaygroundInterface) = {
      Json.obj(
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
    }
}
