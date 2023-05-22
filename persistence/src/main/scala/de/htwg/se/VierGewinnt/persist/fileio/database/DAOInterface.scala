import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

import scala.util.Try

trait DAOInterface[T] {

  def save(playground: PlaygroundInterface): Try[Unit]

  def load(playgroundInterface: PlaygroundInterface): Try[Unit]
}
