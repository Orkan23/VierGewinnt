/** PlaygroundPVE base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl

import de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl.EnemyComputerStrategy
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.{Chip, Grid}
import de.htwg.se.VierGewinnt.model.gridComponent.{GridInterface, gridBaseImpl}
import de.htwg.se.VierGewinnt.model.playerComponent.{PlayerInterface, playerBaseImpl}
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

/** Playground specific for player vs computer. */
case class PlaygroundPvE(val grid: GridInterface, val player: List[PlayerInterface]) extends PlaygroundTemplate :
  def this(size: Int = 7) = this(new Grid(size), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.BotPlayer("Bot 1", Chip.RED)))

  /** Saves the current strategy on how ot handle inserts. */
  val enemStrat = EnemyComputerStrategy()

  /** Insert a Chip onto a specific column and return the new playground. */
  override def insertChip(col: Int): PlaygroundInterface =
    enemStrat.insertChip(this, col)

  /** A automatic process that inserts a Chip onto a specific column and return the new playground. */
  override def computerInsertChip(): PlaygroundInterface =
    enemStrat.computerinsertChip(this)

  /** Take away a chip from a specific column and return the new playground. */
  override def takeAwayChip(col: Int): PlaygroundInterface =
    PlaygroundPvE(grid.removeCell(getDeletePosition(col), col).get, player.reverse)
