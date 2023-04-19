package de.htwg.se.VierGewinnt.persist.fileio


import de.htwg.se.VierGewinnt.persist.fileio.Util.getNameAndChip
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import org.scalatest.matchers.dsl.MatcherWords.be
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.wordspec.AnyWordSpec

class UtilSpec extends AnyWordSpec {
  "The Util singleton" when {
    "called getNameAndChip function" should {
      val resultYellow = getNameAndChip("Player 1&YELLOW")
      val resultRed = getNameAndChip("Player 2&RED")

      "return a tuple name and chip of the player" in {
        resultYellow._1 should be("Player 1")
        resultYellow._2 should be(Chip.YELLOW)

        resultRed._1 should be("Player 2")
        resultRed._2 should be(Chip.RED)
      }
    }
  }
}
