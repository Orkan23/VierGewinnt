import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

import scala.util.Try

trait DAOInterface[T] {

  def create(): Unit

  def read(): String

  def update(input: String): Unit

  def delete(): Unit
}
