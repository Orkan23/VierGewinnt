package service

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import di.CoreModule
import lib.{ControllerInterface, GUI}

object GuiService {
  @main def run(): Unit =
    val injector = Guice.createInjector(new CoreModule)
    val controller = injector.getInstance(classOf[ControllerInterface])

    GUI(controller).main(Array())

}
