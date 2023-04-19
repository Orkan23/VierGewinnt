package de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class HumanPlayerSpec extends AnyWordSpec {
  "A Human Player Base Implementation" when {
    "created" should {
      val spieler = new HumanPlayer("Dummy", Chip.RED)
      "return the Chip" in {
        spieler.getChip() should be(Chip.RED)
      }
      "return the Name" in {
        spieler.getName() should be("Dummy")
      }
    }
  }
}
