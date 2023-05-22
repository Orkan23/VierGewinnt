import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundTemplate

import scala.util.Try

object DAOSlick extends DAOInterface[PlaygroundTemplate] {

  override def create = ???
  //implementation similar to FileIO.scala def save

  override def read = ???
  //implementation similar to FileIO.scala def load

  override def update(input: String) = ???

  override def delete = ???
}
