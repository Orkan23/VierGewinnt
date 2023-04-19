package de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.io.AnsiColor.{BLUE_B, RED_B, YELLOW_B}

class CellSpec extends AnyWordSpec {
  "A Cell" when {
    "Empty" should {
      "be displayed as a Blue Cell" in {
        Cell(Chip.EMPTY).toString() should be(s"${BLUE_B}" + s"   ")
      }
    }
    "picked by Player 1" should {
      "be displayed as a Yellow Cell" in {
        Cell(Chip.YELLOW).toString() should be(s"${YELLOW_B}" + s"   ")
      }
    }
    "picked by Player 2" should {
      "be displayed as a Red Cell" in {
        Cell(Chip.RED).toString() should be(s"${RED_B}" + s"   ")
      }
    }
  }
}
