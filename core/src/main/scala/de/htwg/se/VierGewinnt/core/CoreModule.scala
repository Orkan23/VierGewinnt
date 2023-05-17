package de.htwg.se.VierGewinnt.core

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.VierGewinnt.core.controllerBaseImpl.Controller
import de.htwg.se.VierGewinnt.core.service.{CoreRestController, PlaygroundProvider}
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP


class CoreModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[ControllerInterface])
      .to(classOf[Controller])

    bind(classOf[PlaygroundInterface])
      .annotatedWith(Names.named("DefaultPlayground"))
      .toProvider(classOf[PlaygroundProvider])
    
    bind(classOf[Int])
      .annotatedWith(Names.named("DefaultGameType"))
      .toInstance(0)
    
    bind(classOf[CoreRestController])
}


