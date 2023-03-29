/** EnemyPersonStrategy base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundTemplate

import scala.util.Failure
import scala.util.Success

/** Strategy class, when the opponent is a real person. */
case class EnemyPersonStrategy() extends EnemyStrategy {

  /** Insert a chip to the playground.
   *
   * @param playground Old playground.
   * @param col Column on where to place the chip.
   * @return New playground.
   */
  override def insertChip(pg: PlaygroundTemplate, col: Int): PlaygroundTemplate = {
    val returnGrid = pg.grid.replaceCell(pg.getPosition(col), col, Cell(pg.player(0).getChip()))
    returnGrid match {
      case Success(v) => playgroundBaseImpl.PlaygroundPvP(v, pg.player.reverse) // IF Success, return the new playground
      case Failure(_) => {
        // pg.error = "This column is full try another one"
        pg
      } // If Failure, return the old playground
    }
  }
}
