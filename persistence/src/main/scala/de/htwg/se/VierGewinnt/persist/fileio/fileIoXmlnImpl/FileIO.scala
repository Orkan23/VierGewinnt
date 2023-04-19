/** FileIO XML Implementation for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */
package de.htwg.se.VierGewinnt.persist.fileio.fileIoXmlnImpl

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.BotPlayer
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.HumanPlayer
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.persist.fileio.FileIOInterface
import de.htwg.se.VierGewinnt.persist.fileio.Util.getNameAndChip
import scala.util.Failure
import scala.util.Success
import scala.xml.PrettyPrinter

/** FileIO XML Implementation, to save and load the state of a game with a XML file. */
class FileIO extends FileIOInterface {

  /** Load the game from a "playground.xml" file and return the playground. */
  override def load: PlaygroundInterface =
    val file = scala.xml.XML.loadFile("playground.xml")
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

  /** Save the game to a "playground.json" file.
    *
    * @param playground
    *   The playground to save.
    */

  /** Call the saveString function to save the game to a "playground.xml" file.
    *
    * @param playground
    *   The playground to save.
    */
  override def save(playground: PlaygroundInterface): Unit = saveString(playground)

  /** Save the game to a "playground.xml" file.
    *
    * @param playground
    *   The playground to save.
    */
  def saveString(pg: PlaygroundInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("playground.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(pgToXml(pg))
    pw.write(xml)
    pw.close()
  }

  /** Converts the given playground to XML. */
  def pgToXml(pg: PlaygroundInterface) = {
    <playground size={pg.size.toString} gameType={if pg.isInstanceOf[PlaygroundPvP] then "0" else "1"} player1={
      pg.player(0).getName() + "&" + pg.player(0).getChip()
    } player2={pg.player(1).getName() + "&" + pg.player(1).getChip()}>
      {gridToXmL(pg.grid)}
    </playground>
  }

  /** Converts the given grid to XML. */
  def gridToXmL(grid: GridInterface) =
    for {
      row <- 0 until grid.size
      col <- 0 until grid.size
    } yield {
      <cell row={row.toString} col={col.toString} chip={grid.getCell(row, col).chip.toString}>
      </cell>
    }
}
