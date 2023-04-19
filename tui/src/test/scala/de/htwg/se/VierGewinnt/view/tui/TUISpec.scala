package de.htwg.se.VierGewinnt.view.tui

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.core.{ControllerInterface, CoreModule}
import de.htwg.se.VierGewinnt.core.controllerBaseImpl.{Controller, GameState, PlayState, PrepareState}

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.StringReader
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import java.io.*
import scala.Console.{withIn, withOut}

class TUISpec extends AnyWordSpec {
  "TUI of VierGewinnt" should {
    val injector = Guice.createInjector(new CoreModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    val tui = new TUI(controller)

    "run" in {
      val in = new BufferedReader(new StringReader("\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.run
        }
      }
      source.toString should include("Welcome to 'Vier Gewinnt'")
      source.toString should include("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER")
      source.toString should include("0:'Player vs. Player', 1:'Player vs. Bot', 2:'Bot vs. Bot'")
      source.toString should include(controller.toString)
      source.toString should include("Game is on")
    }

    "choose enemy mode" in {
      val in = new BufferedReader(new StringReader("\n0\n7\nredo\nundo\nk\n4\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.run
        }
      }
      source.toString should include("Game is on")
      source.toString should include("It's your turn Player 2")
      source.toString should include("It's your turn Player 1")
      source.toString should include("wrong input, try a number from 1 to 7")
      source.toString should include("doesn't look like a number")
    }

    "choose no mode" in {
      val in = new BufferedReader(new StringReader("\n2\n7\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.run
        }
      }
      source.toString should include("Game is on")
      source.toString should include("It's your turn Player 1")
      source.toString should include("It's your turn Player 2")
      source.toString should include(controller.toString)
    }

    "valid input" in {
      val in = new BufferedReader(new StringReader("\n1\nq\n"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.getInputAndPrintLoop()
        }
      }
      source.toString should include("doesn't look like a number")
    }

    "restart the game" in {
      val in = new BufferedReader(new StringReader("\nrestart\nq\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.getInputAndPrintLoop()
        }
      }
      source.toString should include("Game is setting up")
      source.toString should include("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER")
    }

    "invalid input: not a number" in {
      val in = new BufferedReader(new StringReader("\na\n \n+\n\nq\n"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.getInputAndPrintLoop()
        }
      }
      source.toString should include("doesn't look like a number")
    }
    "invalid input: a number but not in range" in {
      val in = new BufferedReader(new StringReader("0\n0\n8\n-1\nq\n"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.getInputAndPrintLoop()
        }
      }
      source.toString should include("wrong input, try a number from 1 to " + controller.playground.size)
    }

    "update with update()" in {
      val in = new BufferedReader(new StringReader(""))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.update
        }
      }
      source.toString should include(controller.toString)
    }

    "quit on prepareGameType()" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader("q"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include ("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER\n" +
        "0:'Player vs. Player', 1:'Player vs. Bot', 2:'Bot vs. Bot'")
    }

    "choose 0 and then size 7 on prepareGameType()" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader("0\n7"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("Type the grid size")
    }

    "choose 2 and quit on prepareGameType()" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader("2\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("not supported yet")
    }

    "choose empty and quit on prepareGameType()" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader(" \nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER")
      source.toString should include("0:'Player vs. Player', 1:'Player vs. Bot', 2:'Bot vs. Bot'")
    }

    "should be able to use prepareGameType()" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader(" \n2\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER")
      source.toString should include("0:'Player vs. Player', 1:'Player vs. Bot', 2:'Bot vs. Bot'")
      source.toString should include("not supported yet")
    }

    "should not be able to use prepareGameType()" in {
      controller.gamestate.changeState(PlayState())
      val in = new BufferedReader(new StringReader(" \n2\nq"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("Please select one of the game type you want to play. For default settings ('Player vs Player', grid size=7) press ENTER")
      source.toString should include("0:'Player vs. Player', 1:'Player vs. Bot', 2:'Bot vs. Bot'")
      source.toString should not include("not supported yet")
    }

    "should be able to save and load a game in Prepare State" in {
      controller.gamestate.changeState(PrepareState())
      val in = new BufferedReader(new StringReader("save\nload"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.prepareGameType()
        }
      }
      source.toString should include("Game saved.")
      source.toString should include("Game loaded.")
    }

    "should be able to save and load a game in playing State" in {
      controller.gamestate.changeState(PlayState())
      val in = new BufferedReader(new StringReader("0\n0\nsave\nload\nq\n"))
      val source = new ByteArrayOutputStream()
      val printer = new PrintStream(source)
      withOut(printer) {
        withIn(in) {
          tui.getInputAndPrintLoop()
        }
      }
      source.toString should include("Game saved.")
      source.toString should include("Game loaded.")
    }
  }
}
