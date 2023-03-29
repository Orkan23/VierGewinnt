/** VierGewinnt class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt

import aview.GUI
import aview.TUI
import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.VierGewinnt.controller.controllerComponent.ControllerInterface

import scala.io.StdIn.readLine

/** Main run, parameter decide if run in gui, tui or both. */
@main def run(args:String*): Unit =
  val injector = Guice.createInjector(new VierGewinntModule)
  val controller = injector.getInstance(classOf[ControllerInterface])

  args match
    case x if x contains "tui" =>
      TUI(controller).run
    case x if x contains "gui" =>
      GUI(controller).main(Array())
    case _ =>
      new Thread {
        override def run(): Unit = GUI(controller).main(Array())
      }.start()
      TUI(controller).run
