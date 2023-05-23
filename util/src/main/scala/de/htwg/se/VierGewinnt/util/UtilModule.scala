package de.htwg.se.VierGewinnt.util

import com.google.inject.AbstractModule
import de.htwg.se.VierGewinnt.util.service.UtilRestController

class UtilModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[Observable]).to(classOf[ObservableImpl])
    bind(classOf[UtilRestController])
}
