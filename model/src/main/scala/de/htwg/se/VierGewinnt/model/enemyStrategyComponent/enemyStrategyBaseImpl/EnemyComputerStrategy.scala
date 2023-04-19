/** EnemyComputerStrategy base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundTemplate
import scala.util.Failure
import scala.util.Random
import scala.util.Success

/** Strategy class, when the opponent is a computer. */
case class EnemyComputerStrategy() extends EnemyStrategy {

  /** Insert a chip to the playground.
   *
   * @param playground Old playground.
   * @param col Column on where to place the chip.
   * @return New playground.
   */
  override def insertChip(pg: PlaygroundTemplate, col: Int): PlaygroundTemplate = {
    var temp =
      pg // Temporary Playground, only for EnemyComputer, because of the Full Grid check. If Grid Full, Computer does NOT play anymore.
    val returnGrid = pg.grid.replaceCell(pg.getPosition(col), col, Cell(pg.player(0).getChip()))
    returnGrid match {
      case Success(v) => temp = PlaygroundPvE(v, pg.player) // IF Success, return the new playground
      case Failure(_) =>
        temp = {
          pg.error = "This column is full try another one"
          pg } // If Failure, return the old playground
    }
    temp
  }

  /** Function for the computer to place a chip.
   *
   * @param pg Old playground.
   * @return Returns the new playground. If the playground is full, then the same old playground gets returned.
   */
  def computerinsertChip(pg: PlaygroundTemplate): PlaygroundTemplate = {
    pg.grid.checkWin() match
      case Some(_) => pg
      case None =>
        var chosenCol = Random.between(0, pg.size)
        for (i <- 0 to pg.size - 1)
          if (pg.getPosition(chosenCol) == -1) {
            chosenCol = i
          }

        val returnGrid = pg.grid.replaceCell(pg.getPosition(chosenCol), chosenCol, gridBaseImpl.Cell(pg.player(1).getChip()))
        returnGrid match
          case Success(v) => playgroundBaseImpl.PlaygroundPvE(v, pg.player) // IF Success, return the new playground
          case Failure(_) => pg // If Failure, retry inserting a chip!
  }
}
