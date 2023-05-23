package de.htwg.se.VierGewinnt.view.gui

import com.google.inject.AbstractModule

class GuiModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[GuiRestController])
    bind(classOf[GUI])
}
