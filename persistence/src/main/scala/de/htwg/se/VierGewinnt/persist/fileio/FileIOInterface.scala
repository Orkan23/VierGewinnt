package de.htwg.se.VierGewinnt.persist.fileio

import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

/** Interface for FileIO, to save and load the state of a game. */
trait FileIOInterface {

  /** Load the game from a file and return the playground. */
  def load: PlaygroundInterface

  /** Save the game to a file.
    *
    * @param playground
    *   The playground to save.
    */
  def save(playground: PlaygroundInterface): Unit
}
