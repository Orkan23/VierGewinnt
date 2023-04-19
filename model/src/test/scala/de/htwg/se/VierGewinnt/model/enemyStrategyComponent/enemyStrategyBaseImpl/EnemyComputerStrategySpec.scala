package de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent
import de.htwg.se.VierGewinnt.model.gridComponent.*
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.{BotPlayer, HumanPlayer}
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.io.AnsiColor.*

class EnemyComputerStrategySpec extends AnyWordSpec {
  "An Enemy Computer Spec should work random" when {
    var playground = PlaygroundPvE(new Grid(7), List(HumanPlayer("Player 1", Chip.YELLOW), BotPlayer("Bot 1", Chip.RED)))
    "Inserting a Chip" in {
      playground.insertChip(1).toString should not be (
        PlaygroundPvE(
          Grid(Vector.tabulate(7, 7)((row, col) => Cell(Chip.EMPTY))).replaceCellRisk(6, 1, Cell(Chip.YELLOW)),
          List(playerBaseImpl.HumanPlayer("Player 2", Chip.RED), playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW))
        ).toString
      )
    }
    "return old playground when index out of bounds" in {
      playground.insertChip(10) should be (playground)
    }
    "Getting the Strategy" in {
      playground.enemStrat should be (EnemyComputerStrategy())
    }
    "not inserting a Chip manually" in {
      var playground_ins1 = playground.insertChip(1)
      var playground_ins2 = playground_ins1.insertChip(1)
      var playground_ins3 = playground_ins2.insertChip(1)
      var playground_ins4 = playground_ins3.insertChip(1)
      var playground_ins5 = playground_ins4.insertChip(1)
      var playground_ins6 = playground_ins5.insertChip(1)
      var playground_full = playground_ins6.insertChip(1)
      playground_full.insertChip(1) should be(playground_full)
    }
    "not inserting a Chip by computer" in {
      var playground_comp = PlaygroundPvE(new Grid(1), List(HumanPlayer("Player 1", Chip.YELLOW), BotPlayer("Bot 1", Chip.RED)))
      var playground_ins1 = playground_comp.insertChip(0)
      playground_ins1.insertChip(0) should be(playground_ins1)
    }
    "not insert when full by computer" in {
      var pl = PlaygroundPvE(
        Grid(Vector.tabulate(2, 2)((row, col) => Cell(Chip.EMPTY)))
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(0, 1, Cell(Chip.RED))
          .replaceCellRisk(1, 0, Cell(Chip.YELLOW))
          .replaceCellRisk(1, 1, Cell(Chip.YELLOW)),
        List(HumanPlayer("Player 1", Chip.YELLOW), BotPlayer("Bot 1", Chip.RED))
      )
      pl.computerInsertChip() should be(pl)
    }
    "not insert a chip when someone has won by computer" in {
      var pl = PlaygroundPvE(
        Grid(Vector.tabulate(4, 4)((row, col) => Cell(Chip.EMPTY)))
          .replaceCellRisk(0, 0, Cell(Chip.YELLOW))
          .replaceCellRisk(1, 0, Cell(Chip.YELLOW))
          .replaceCellRisk(2, 0, Cell(Chip.YELLOW))
          .replaceCellRisk(3, 0, Cell(Chip.YELLOW)),
        List(HumanPlayer("Player 1", Chip.YELLOW), BotPlayer("Bot 1", Chip.RED))
      )
      pl.computerInsertChip() should be(pl)
    }
    "insert random by computer" in {
      var playground_comp = PlaygroundPvE(new Grid(1), List(HumanPlayer("Player 1", Chip.YELLOW), BotPlayer("Bot 1", Chip.RED)))
      playground_comp.computerInsertChip() should not be(playground_comp)
    }
  }
}
