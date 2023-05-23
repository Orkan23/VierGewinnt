package de.htwg.se.VierGewinnt.model

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.service.ModelRestController


class ModelModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[ModelRestController])
    bind(classOf[PlaygroundInterface]).toInstance(new PlaygroundPvP)
    bind(classOf[GridInterface]).toInstance(new Grid)
}
