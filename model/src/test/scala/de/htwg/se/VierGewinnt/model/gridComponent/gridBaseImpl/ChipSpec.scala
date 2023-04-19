package de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.io.AnsiColor.*

class ChipSpec extends AnyWordSpec {
  "Create chip for empty cell" when {
    val chip = Chip.EMPTY
    "have Value 0" in {
      chip.getValue should be(0)
    }
    "have Color Blue" in {
      chip.getColorCode should be(BLUE_B)
    }
  }
  "Create Chip for Red Cell" when {
    val chip = Chip.RED
    "have Value 1" in {
      chip.getValue should be(1)
    }
    "have Color Red" in {
      chip.getColorCode should be(RED_B)
    }
  }
  "Create Chip for Yellow Cell" when {
    val chip = Chip.YELLOW
    "have Value 2" in {
      chip.getValue should be(2)
    }
    "have Color Yellow" in {
      chip.getColorCode should be(YELLOW_B)
    }
  }
}
