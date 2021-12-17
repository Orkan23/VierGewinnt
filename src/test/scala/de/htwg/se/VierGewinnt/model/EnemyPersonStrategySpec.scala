package de.htwg.se.VierGewinnt.model

import de.htwg.se.VierGewinnt.controller.Controller
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import scala.io.AnsiColor.*

class EnemyPersonStrategySpec extends AnyWordSpec {
  "An Enemy Person Spec should work deterministic" when {
    var playground = PlaygroundPvP(new Grid(7), List(HumanPlayer("Player 1", Chip.YELLOW), HumanPlayer("Player 2", Chip.RED)))
    "Inserting a Chip" in {
      playground.insertChip(1) should be(
        PlaygroundPvP(
          Grid(Vector.tabulate(7, 7)((row, col) => Cell(Chip.EMPTY))).replaceCellRisk(6, 1, Cell(Chip.YELLOW)),
          List(HumanPlayer("Player 2", Chip.RED), HumanPlayer("Player 1", Chip.YELLOW))
        )
      )
    }
    "Inserting a Chip in full column" in {

      var playground_ins1 = playground.insertChip(1)
      var playground_ins2 = playground_ins1.insertChip(1)
      var playground_ins3 = playground_ins2.insertChip(1)
      var playground_ins4 = playground_ins3.insertChip(1)
      var playground_ins5 = playground_ins4.insertChip(1)
      var playground_full = playground_ins5.insertChip(1)

      playground_full.insertChip(1) should be(
        PlaygroundPvP(
          Grid(Vector.tabulate(7, 7)((row, col) => Cell(Chip.EMPTY)))
            .replaceCellRisk(0, 1, Cell(Chip.YELLOW))
            .replaceCellRisk(1, 1, Cell(Chip.RED))
            .replaceCellRisk(2, 1, Cell(Chip.YELLOW))
            .replaceCellRisk(3, 1, Cell(Chip.RED))
            .replaceCellRisk(4, 1, Cell(Chip.YELLOW))
            .replaceCellRisk(5, 1, Cell(Chip.RED))
            .replaceCellRisk(6, 1, Cell(Chip.YELLOW)),
          List(HumanPlayer("Player 2", Chip.RED), HumanPlayer("Player 1", Chip.YELLOW))
        )
      )
    }
  }
}
