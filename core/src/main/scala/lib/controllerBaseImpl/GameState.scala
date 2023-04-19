/** GameState class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package lib.controllerBaseImpl

import de.htwg.se.VierGewinnt.util.State

/** The state in which the game can be. */
case class GameState() {
  /** Variable state saves the State.
   * On creating, defaults the PrepareState. */
  var state: State[GameState] = PrepareState()

  /** Returns the individual string of each State. */
  def displayState(): String = state.displayState

  /** Change the state.
   *
   * @param state The new state of the GameState.
   */
  def changeState(state: State[GameState]): Unit = this.state = state
}

/** Preparing State of the game. */
case class PrepareState()extends State[GameState] :
  override def displayState: String = "Game is setting up"

/** Play State of the game. */
case class PlayState()extends State[GameState] :
  override def displayState: String = "Game is on"

/** Win State of the game. */
case class WinState()extends State[GameState] :
  override def displayState: String = "Game is won"

/** Tie State of the game. */
case class TieState()extends State[GameState] :
  override def displayState: String = "Game ends with a tie"

/** End State of the game. */
case class EndState()extends State[GameState] :
  override def displayState: String = "Game over. Restart?"
