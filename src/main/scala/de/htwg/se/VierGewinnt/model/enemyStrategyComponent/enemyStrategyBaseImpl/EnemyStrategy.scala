/** EnemyStrategy Interface for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl

import de.htwg.se.VierGewinnt.model.enemyStrategyComponent.EnemyStrategyInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundTemplate

/** Strategy Pattern, which decides if you play against a computer or actual person */
trait EnemyStrategy extends EnemyStrategyInterface:

  /** Insert a chip to the playground.
   *
   * @param playground Old playground.
   * @param col Column on where to place the chip.
   * @return New playground.
   */
  def insertChip(playground: PlaygroundTemplate, col: Int): PlaygroundTemplate
