package de.htwg.se.VierGewinnt.core

import de.htwg.se.VierGewinnt.core.controllerBaseImpl.InsertChipCommand
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.{Chip, Grid}
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.util.Move
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class InsertChipCommandSpec extends AnyWordSpec  {
  "A InsertChipCommand Base Implementation" when {
    val command = new InsertChipCommand(Move(0))
    val playground = playgroundBaseImpl.PlaygroundPvP(new Grid(7), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))

    "should do" should {
      val playground_stepdone = playground.insertChip(0)
      val playground_stepundo = playground_stepdone.takeAwayChip(0)
      val playground_stepredo = playground_stepundo.insertChip(0)
      "noStep" in {
        command.noStep(playground) should be (playground)
      }
      "doStep" in {
        command.doStep(playground) should be (playground_stepdone)
      }
      "undoStep" in {
        command.undoStep(playground_stepdone) should be (playground_stepundo)
      }
      "redoStep" in {
        command.redoStep(playground_stepundo) should be (playground_stepredo)
      }
    }
  }
}
