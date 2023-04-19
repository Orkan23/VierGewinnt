package de.htwg.se.VierGewinnt.model.enemyStrategyComponent.enemyStrategyMockImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.VierGewinnt.model.playerComponent.playerBaseImpl.HumanPlayer
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class EnemyStrategySpec extends AnyWordSpec {
  "An EnemyStrategySpec Mock Implementation" when {
    "initialized" should {
      val enemStrat = new EnemyStrategy
      val playtemp = PlaygroundPvP(new Grid(7), List(HumanPlayer("Player 1", Chip.YELLOW), HumanPlayer("Player 2", Chip.RED)))
      "return playground" in {
        enemStrat.insertChip(playtemp, 0) should be(playtemp)
      }
    }
  }
}
