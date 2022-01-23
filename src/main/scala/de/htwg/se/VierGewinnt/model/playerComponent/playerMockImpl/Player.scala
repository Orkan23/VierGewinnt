/** Player mock implementation for VierGewinnt.
 *
 * @author Thu Ha Dinh & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playerComponent.playerMockImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.playerComponent.PlayerInterface

class Player(val name: String, val chip: Chip) extends PlayerInterface:
  override def getChip(): Chip = Chip.EMPTY
  override def getName(): String = ""
