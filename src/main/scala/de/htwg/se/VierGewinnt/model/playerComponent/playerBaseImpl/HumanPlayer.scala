/** HumanPlayer base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip

import scala.io.AnsiColor.RESET

/** Class for a Human Player. */
class HumanPlayer(val name: String, val chip: Chip) extends Player:

  /** Returns the name with the chip color code. */
  override def toString: String = s"${chip.getColorCode}$name${RESET}"
