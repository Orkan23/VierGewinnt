package de.htwg.se.VierGewinnt.core

import de.htwg.se.VierGewinnt.core.controllerBaseImpl.GameState
import de.htwg.se.VierGewinnt.model.playerComponent.PlayerInterface
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.util.{Move, Observable}

/** Interface for the controller. Extends the Observable class to be compatible with the model-view-controller architecture and communicate
  * vith the view.
  */
trait ControllerInterface:
  /** Returns the size of the grid withing playground. */
  def gridSize: Int

  /** Sets up a new game and switches the GameState to PlayState().
    *
    * @param gameType
    *   Choose the gametype (0 -> PVP, 1 -> PVP).
    * @param size
    *   Choose the size of the playground.
    */
  def setupGame(gameType: Int, size: Int): Unit

  /** Loads the previously saved playground from a file. */
  def load: Unit

  /** Saves the current playground to a file. */
  def save: Unit

  /** Reset the GameState to PrepareState() to restart the game. */
  def restartGame: Unit

  /** Checks if the game is still in preparing phase. */
  def isPreparing: Boolean

  /** Checks if the game is not over yet. */
  def gameNotDone: Boolean

  /** Do a given move with a given function and save it into the UndoManager.
    *
    * @param doThis
    *   A function to do and save into the UndoManager.
    * @param move
    *   Move with input.
    */
  def doAndPublish(doThis: Move => PlaygroundInterface, move: Move): Unit

  /** Do a given function and save it into the UndoManager.
    *
    * @param doThis
    *   A function to do and save into the UndoManager.
    */
  def doAndPublish(doThis: => PlaygroundInterface): Unit

  /** Undo the last move. */
  def undo: PlaygroundInterface

  /** Redo the last undone move. */
  def redo: PlaygroundInterface

  /** Insert a chip into the playground.
    *
    * @param move
    *   On which column the chip should be placed on the playground.
    * @return
    *   Return a new playground after the chip was inserted.
    */
  def insChip(move: Move): PlaygroundInterface

  /** Check if the playground is full with chips. */
  def checkFull(pg: PlaygroundInterface): Unit

  /** Check if the playground has a winner. */
  def checkWinner(pg: PlaygroundInterface): Unit

  var winnerChips: Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None

  /** Gets the color of a chip on a certain coordinate.
    *
    * @param row
    *   The row (y-coordinate) of the playground.
    * @param col
    *   The column (x-coordinate) of the playground.
    * @return
    *   Returns the color chip in that position in ANSII.
    */
  def getChipColor(row: Int, col: Int): String

  /** Gets the string of the current state.
    *
    * @return
    *   Returns the string of the current state.
    */
  def printState: String

  /** Gets the status string of the playground.
    *
    * @return
    *   Returns the status string of the playground.
    */
  def playgroundState: String

  /** Variable for the current GameState. */
  var gamestate: GameState = GameState()

  /** Variable for the list of players. */
  var player: List[PlayerInterface] = List()

  /** Variable for the playground. */
  var playground: PlaygroundInterface
