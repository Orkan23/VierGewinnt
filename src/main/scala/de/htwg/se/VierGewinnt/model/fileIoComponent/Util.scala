package de.htwg.se.VierGewinnt.model.fileIoComponent

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip

object Util {
  def getNameAndChip(playerName: String): (String, Chip) =
    (playerName.split("&")(0), if (playerName.split("&")(1)) == "RED" then Chip.RED else Chip.YELLOW)
}
