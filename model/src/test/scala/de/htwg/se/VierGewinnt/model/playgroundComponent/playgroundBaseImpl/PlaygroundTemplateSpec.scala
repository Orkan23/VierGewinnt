package de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyBaseImpl.*
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.HumanPlayer
import de.htwg.se.VierGewinnt.model.playerComponent.{playerBaseImpl, playerMockImpl}
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.io.AnsiColor.*
import scala.math.ceil

class PlaygroundTemplateSpec extends AnyWordSpec {
  "Playground is the representation of the gamefield" when {
    "initialized without parameter" should {
      val playground = PlaygroundPvP(new Grid(7), List(HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
      "have the default size" in {
        playground.size should be(7)
      }
      val playgroundcpy = playground.insertChip(0)
      "Player should insert a chip in a choosen column" in {
        playgroundcpy.grid.getCell(playgroundcpy.size - 1, 0).chip should be(Chip.YELLOW)
      }
      "request how many chips inserted in a column" in {
        playgroundcpy.getPosition(0) should be(playgroundcpy.size - 2)
      }
      "return 0 on too big getPosition input" in {
        playgroundcpy.getPosition(100) should be(0)
      }
      "return 0 on too low getPosition input" in {
        playgroundcpy.getPosition(-100) should be(0)
      }
      "be able to return the last position" in {
        playgroundcpy.getDeletePosition(0) should be(playgroundcpy.size - 1)
      }
    }
    "have a String representation with a head, grid and border" should {
      val playground = PlaygroundPvP(new Grid(7), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
      "have a line with column names" in {
        playground.colnames() should be(s"${BLUE_B}\t1\t2\t3\t4\t5\t6\t7\t ${RESET}\n")
      }
      "have a bottom border" in {
        playground.border() should be(s"${BLUE_B}  -----------------------------  ${RESET}\n")
      }
      "should print the whole gamefield" in {
        playground.toString should be("It's your turn " + playground.player(0).getName() + "\n" + playground.colnames() + playground.grid + playground.border())
      }
    }
    "when a column is full" should {
      val playground = new PlaygroundPvP(new Grid(1), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
      val playgroundfull = playground.insertChip(0)
      "nothing happen" in {
        playgroundfull.insertChip(0) should be(playgroundfull)
      }
      "print that the col is full" in {
        playgroundfull.insertChip(0).error = "This column is full try another one"
      }
    }
    "when getting the Status" should {
      "Yellow wins" in {
        val playground = PlaygroundPvP(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
        val playground_ylmove1 = playground.insertChip(0)
        val playground_redmove1 = playground_ylmove1.insertChip(1)
        val playground_ylmove2 = playground_redmove1.insertChip(0)
        val playground_redmove2 = playground_ylmove2.insertChip(2)
        val playground_ylmove3 = playground_redmove2.insertChip(0)
        val playground_redmove3 = playground_ylmove3.insertChip(3)
        val playground_ylmove4 = playground_redmove3.insertChip(0)
        val playground_redmove4 = playground_ylmove4.insertChip(0)
        playground_redmove4.getStatus() should be ("Player Yellow has won the game!")
      }
      "Red wins" in {
        val playground = PlaygroundPvP(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
        val playground_ylmove1 = playground.insertChip(1)
        val playground_redmove1 = playground_ylmove1.insertChip(0)
        val playground_ylmove2 = playground_redmove1.insertChip(2)
        val playground_redmove2 = playground_ylmove2.insertChip(0)
        val playground_ylmove3 = playground_redmove2.insertChip(3)
        val playground_redmove3 = playground_ylmove3.insertChip(0)
        val playground_ylmove4 = playground_redmove3.insertChip(2)
        val playground_redmove4 = playground_ylmove4.insertChip(0)
        playground_redmove4.getStatus() should be ("Player Red has won the game!")
      }
      "no one won yet" in {
          val playground = PlaygroundPvP(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
          val playground_ylmove1 = playground.insertChip(0)
          val playground_redmove1 = playground_ylmove1.insertChip(1)
          playground_redmove1.getStatus() should be ("It's your turn Player 1")
      }
    }
    "when initialized as PVE" should {
      "return the correct enemStrat PVE" in  {
        val playground = PlaygroundPvE(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.BotPlayer("Bot", Chip.RED)))
        val getStrat = playground.enemStrat
        getStrat should be (EnemyComputerStrategy())
      }
      "set a chip correctly" in {
        val playground = PlaygroundPvE(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.BotPlayer("Bot", Chip.RED)))
        playground.computerInsertChip() should not be (playground)
      }
    }
    "when initialized as PVP" should {
      "return the correct enemStrat PVP" in  {
        val playground = PlaygroundPvP(new Grid(4), List(playerBaseImpl.HumanPlayer("Player 1", Chip.YELLOW), playerBaseImpl.HumanPlayer("Player 2", Chip.RED)))
        val getStrat = playground.enemStrat
        getStrat should be (EnemyPersonStrategy())
      }
    }
  }
}

