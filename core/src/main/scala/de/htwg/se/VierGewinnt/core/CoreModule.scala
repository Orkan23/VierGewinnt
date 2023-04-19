package de.htwg.se.VierGewinnt.core

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.VierGewinnt.core.controllerBaseImpl.Controller
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.persist.fileio.FileIOInterface
import de.htwg.se.VierGewinnt.persist.fileio.fileIoJsonImpl.FileIO as FileIoXML
import de.htwg.se.VierGewinnt.persist.fileio.fileIoJsonImpl.FileIO as FileIoJson

class CoreModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[ControllerInterface]).to(classOf[Controller])

    bind(classOf[PlaygroundInterface]).annotatedWith(Names.named("DefaultPlayground")).toInstance(new PlaygroundPvP())
    bind(classOf[Int]).annotatedWith(Names.named("DefaultGameType")).toInstance(0)


    bind(classOf[FileIOInterface]).to(classOf[FileIoXML])
    //bind(classOf[FileIOInterface]).to(classOf[FileIoJson])
}
