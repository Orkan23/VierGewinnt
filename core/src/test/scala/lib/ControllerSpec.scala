package lib

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.VierGewinntModule
import de.htwg.se.VierGewinnt.model.*
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.*
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.util.{Move, Observer}
import lib.ControllerInterface
import lib.controllerBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.io.AnsiColor.{BLUE_B, RED_B, YELLOW_B}
import scala.io.Source

class ControllerSpec extends AnyWordSpec {
  "A controller" when {
    "observed by an Observer" should {
      val injector = Guice.createInjector(new VierGewinntModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      val observer = new Observer {
        var updated: Boolean = false

        override def update: Unit = updated = !updated

        override def toString: String = updated.toString
      }

      "notify when insert a chip" in {
        controller.add(observer)
        controller.doAndPublish(controller.insChip, Move(0))
        observer.toString should be("true")
        controller.playground.getPosition(0) should be(5)
      }

      "after remove should not notify" in {
        controller.remove(observer)
        controller.doAndPublish(controller.insChip, Move(0))
        observer.toString should be("true")
      }

      "check if the Board is full" in {
        controller.setupGame(0, 2)
        controller.doAndPublish(controller.insChip, Move(0))
        controller.doAndPublish(controller.insChip, Move(0))
        controller.doAndPublish(controller.insChip, Move(1))
        controller.doAndPublish(controller.insChip, Move(1))
        controller.checkFull(controller.playground)
        controller.gamestate.state should be (TieState())
      }

      "setup a PVE Game" in {
        controller.setupGame(1, 7)
        controller.playground.player(1).getName() should be ("Bot 1")
        controller.gamestate.state should be (PlayState())
      }

      "setup a PVP Game" in {
        controller.setupGame(0, 7)
        controller.playground.player(1).getName() should be ("Player 2")
        controller.gamestate.state should be (PlayState())
      }


      "check if there is a winner" in {
        controller.doAndPublish(controller.insChip, Move(0))
        controller.doAndPublish(controller.insChip, Move(1))
        controller.doAndPublish(controller.insChip, Move(0))
        controller.doAndPublish(controller.insChip, Move(1))
        controller.doAndPublish(controller.insChip, Move(0))
        controller.doAndPublish(controller.insChip, Move(1))
        controller.doAndPublish(controller.insChip, Move(0))
        controller.checkWinner(controller.playground)
        controller.gamestate.state should be (WinState())
      }

      "get the Chip Color" in {
        controller.getChipColor(0,0) should be (BLUE_B)
      }

      "get the playground state" in {
        controller.toString
        controller.playgroundState should be ("Player Yellow has won the game!")
      }

      "undo and redo a move" in {
        val injector = Guice.createInjector(new VierGewinntModule)
        val controller2 = injector.getInstance(classOf[ControllerInterface])
        var field = controller2.playground
        controller2.doAndPublish(controller2.insChip, Move(0))
        controller2.playground.grid.getCell(6, 0) should be(Cell(Chip.YELLOW))
        controller2.doAndPublish(controller2.undo);
        controller2.playground.grid.getCell(6, 0) should be(gridBaseImpl.Cell(Chip.EMPTY))
        controller2.doAndPublish(controller2.redo);
        controller2.playground.grid.getCell(6, 0) should be(gridBaseImpl.Cell(Chip.YELLOW))
      }
      "return grid size" in {
        controller.gridSize should be(7)
      }
      "return if it is preparing" in {
        controller.gamestate.changeState(PlayState())
        controller.isPreparing should be(false)
      }
      "restart the game to reset into PrepareState" in {
        controller.restartGame
        controller.gamestate.state should be(PrepareState())
      }
      "game is not done yet" in {
        controller.gamestate.changeState(PlayState())
        controller.gameNotDone should be(true)
      }
      "save a game" in {
        controller.gamestate.changeState(PlayState())
        controller.save
        val result = Source.fromFile("playground.json").getLines().mkString
        result should (include("<playground") or include(s"\"playground\""))
      }

      "load a game" in {
        controller.setupGame(0,7)
        val savedBeforeChange = controller.playground
        controller.save
        controller.insChip(Move(0))
        controller.load
        controller.playground.size should be(savedBeforeChange.size)
      }
    }
  }
}
