package di

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.VierGewinnt.model.fileIoComponent.{FileIOInterface, fileIoJsonImpl}
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import lib.controllerBaseImpl.Controller
import lib.ControllerInterface

class CoreModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[ControllerInterface]).to(classOf[Controller])

    bind(classOf[PlaygroundInterface]).annotatedWith(Names.named("DefaultPlayground")).toInstance(new PlaygroundPvP())
    bind(classOf[Int]).annotatedWith(Names.named("DefaultGameType")).toInstance(0)


    bind(classOf[FileIOInterface]).to(classOf[fileIoJsonImpl.FileIO])
  //bind(classOf[FileIOInterface]).to(classOf[fileIoXmlnImpl.FileIO])
}
