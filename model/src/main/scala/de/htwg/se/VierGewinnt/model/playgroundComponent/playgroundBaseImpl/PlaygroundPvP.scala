/** PlaygroundPVP base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl

import de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl.EnemyPersonStrategy
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.{Chip, Grid}
import de.htwg.se.VierGewinnt.model.gridComponent.{GridInterface, gridBaseImpl}
import de.htwg.se.VierGewinnt.model.playerComponent.{PlayerInterface, playerBaseImpl}
import de.htwg.se.VierGewinnt.model.playgroundComponent.{PlaygroundInterface, playgroundBaseImpl}

/** Playground specific for player vs player. */
case class PlaygroundPvP(val grid: GridInterface, val player: List[PlayerInterface]) extends PlaygroundTemplate {
  def this(size: Int = 7) = this(new Grid(size), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))

  /** Saves the current strategy on how ot handle inserts. */
  val enemStrat = EnemyPersonStrategy()

  /** Insert a Chip onto a specific column and return the new playground. */
  override def insertChip(col: Int): PlaygroundInterface =
    enemStrat.insertChip(this, col)

  /** Take away a chip from a specific column and return the new playground. */
  override def takeAwayChip(col: Int): PlaygroundInterface =
    PlaygroundPvP(grid.removeCell(getDeletePosition(col), col).get, player.reverse)

  /** A automatic process that inserts a Chip onto a specific column and return the new playground.
   * Here, because it is player vs player, we don't need this and return the same playground.*/
  override def computerInsertChip(): PlaygroundInterface = this //never used
}
