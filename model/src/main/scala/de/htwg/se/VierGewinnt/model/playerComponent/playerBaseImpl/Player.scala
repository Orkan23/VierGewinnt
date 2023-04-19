/** Player Interface base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.playerComponent.PlayerInterface

/** Interface that extends the PlayerInterface to choose between human and bot players. */
trait Player() extends PlayerInterface:
  val name: String
  val chip: Chip

  /** Get the user-specific chip. */
  override def getChip(): Chip = chip

  /** Get the name of the player. */
  override def getName(): String = name
