package de.htwg.se.VierGewinnt.model.playerComponent.playerMockImpl

import de.htwg.se.VierGewinnt.model.playerComponent.playerMockImpl
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec {
  "A Player Mock Implementation" when {
    "created" should {
      val spieler = new Player("Dummy", Chip.RED)
      "return the Chip" in {
        spieler.getChip() should be (Chip.EMPTY)
      }
      "return the Name" in {
        spieler.getName() should be ("")
      }
    }
  }
}
