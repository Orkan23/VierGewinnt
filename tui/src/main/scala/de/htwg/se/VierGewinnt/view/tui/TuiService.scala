package de.htwg.se.VierGewinnt.view.tui

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.core.ControllerInterface
import de.htwg.se.VierGewinnt.core.CoreModule

object TuiService {
  @main def run(): Unit =
    val injector = Guice.createInjector(new CoreModule)
    val controller = injector.getInstance(classOf[ControllerInterface])

    TUI(controller).run
}
