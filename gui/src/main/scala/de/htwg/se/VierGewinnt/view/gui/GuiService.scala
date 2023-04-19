package de.htwg.se.VierGewinnt.view.gui

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.core.ControllerInterface
import de.htwg.se.VierGewinnt.core.CoreModule

object GuiService {
  @main def run(): Unit =
    val injector = Guice.createInjector(new CoreModule)
    val controller = injector.getInstance(classOf[ControllerInterface])

    GUI(controller).main(Array())

}
