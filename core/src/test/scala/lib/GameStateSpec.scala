package lib

import de.htwg.se.VierGewinnt.model.*
import de.htwg.se.VierGewinnt.util.Observer
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import lib.controllerBaseImpl.{EndState, GameState, PlayState, PrepareState, TieState, WinState}

class GameStateSpec extends AnyWordSpec {
  "A GameStateSpec" when {
    "being initialized" should {
      var gamestate: GameState = GameState()
      "have PrepareState" in {
        gamestate.state should be(PrepareState())
      }
      "output preparing" in {
        var gamestate: GameState = GameState()
        gamestate.displayState()
        gamestate.displayState() should be("Game is setting up")
      }
    }
    "changed to PlayState" should {
      var gamestate: GameState = GameState()
      gamestate.changeState(PlayState())
      "have PlayState" in {
        gamestate.state should be(PlayState())
      }
      "output playing" in {
        gamestate.displayState()
        gamestate.displayState() should be("Game is on")
      }
    }
    "being won" should {
      var gamestate: GameState = GameState()
      gamestate.changeState(WinState())
      "have WinState" in {
        gamestate.state should be(WinState())
      }
      "output won" in {
        gamestate.displayState()
        gamestate.displayState() should be("Game is won")
      }
    }
    "being tied" should {
      var gamestate: GameState = GameState()
      gamestate.changeState(TieState())
      "have TieState" in {
        gamestate.state should be(TieState())
      }
      "output tie" in {
        gamestate.displayState()
        gamestate.displayState() should be("Game ends with a tie")
      }
    }
    "being over" should {
      var gamestate: GameState = GameState()
      gamestate.changeState(EndState())
      "have EndState" in {
        gamestate.state should be(EndState())
      }
      "output ending" in {
        gamestate.displayState()
        gamestate.displayState() should be("Game over. Restart?")
      }
    }
  }
}
