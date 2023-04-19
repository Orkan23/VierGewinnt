package service

import com.google.inject.Guice
import di.CoreModule
import lib.{ControllerInterface, TUI}

object TuiService {
  @main def run(): Unit =
    val injector = Guice.createInjector(new CoreModule)
    val controller = injector.getInstance(classOf[ControllerInterface])

    TUI(controller).run

}
