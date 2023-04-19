package de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class BotPlayerSpec extends AnyWordSpec {
  "A Bot Player Base Implementation" when {
    "created" should {
      val spieler = new BotPlayer("Dummy", Chip.RED)
      "return the Chip" in {
        spieler.getChip() should be(Chip.RED)
      }
      "return the Name" in {
        spieler.getName() should be("Dummy")
      }
    }
  }
}
