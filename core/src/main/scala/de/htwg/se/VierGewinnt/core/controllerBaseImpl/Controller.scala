/** Controller base implementation for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */

package de.htwg.se.VierGewinnt.core.controllerBaseImpl

import akka.actor.typed.scaladsl.AskPattern.*
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.actor.TypedActor.context
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.Http
import com.google.inject.name.Named
import com.google.inject.name.Names
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Key
import concurrent.duration.Duration
import concurrent.duration.DurationInt
import de.htwg.se.VierGewinnt.core.service.CoreRestController
import de.htwg.se.VierGewinnt.core.service.CoreRestService.getClass
import de.htwg.se.VierGewinnt.core.service.PlaygroundProvider
import de.htwg.se.VierGewinnt.core.ControllerInterface
import de.htwg.se.VierGewinnt.core.CoreModule
import de.htwg.se.VierGewinnt.core.Util
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvE
import de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl.PlaygroundPvP
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface
import de.htwg.se.VierGewinnt.util.Command
import de.htwg.se.VierGewinnt.util.Move
import de.htwg.se.VierGewinnt.util.UndoManager
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

/** Controller base implementation.
  *
  * @param playground
  *   Sets the default playground with inject.
  * @param gameType
  *   Sets the default gametype with inject.
  */
class Controller @Inject() (
    @Named("DefaultPlayground") var playground: PlaygroundInterface,
    @Named("DefaultGameType") var gameType: Int,
    restController: CoreRestController
) extends ControllerInterface:
  val fileIOServer = "http://localhost:8081/fileio"
  val modelServer = "http://localhost:8082"
  private val logger = LoggerFactory.getLogger(getClass)

  /** Returns the size of the grid withing playground. */
  override def gridSize: Int =
    playground.size

  /** Loads the previously saved playground from a file. */
  override def load: Unit =
    val playgroundFuture: Future[String] = restController.sendGetRequest(fileIOServer + "/load")
    val playgroundResult: String = Await.result(playgroundFuture, Duration.Inf)
    logger.info(playgroundResult)
    playground = Util.jsonToPlayground(playgroundResult)
    restController.notifyObservers

  /** Saves the current playground to a file. */
  override def save: Unit =
    val playgroundFuture: Future[String] = restController.sendPostRequest(fileIOServer + "/save", Util.toJsonString(playground))
    val playgroundResult: String = Await.result(playgroundFuture, Duration.Inf)
    logger.info(playgroundResult)
    restController.notifyObservers

  /** Sets up a new game and switches the GameState to PlayState().
    *
    * @param gameType
    *   Choose the gametype (0 -> PVP, 1 -> PVP).
    * @param size
    *   Choose the size of the playground.
    */
  override def setupGame(gameType: Int, size: Int): Unit =
    gameType match
      case 0 =>
        playground = new PlaygroundPvP(size)
      case 1 =>
        playground = new PlaygroundPvE(size)
    gamestate.changeState(PlayState())
    winnerChips = None
    restController.notifyObservers

  /** Variable for an instance of the UndoManager. */
  val undoManager = new UndoManager[PlaygroundInterface]

  /** Do a given move with a given function and save it into the UndoManager.
    *
    * @param doThis
    *   A function to do and save into the UndoManager.
    * @param move
    *   Move with input.
    */
  override def doAndPublish(doThis: Move => PlaygroundInterface, move: Move): Unit =
    gameNotDone match
      case true =>
        playground = doThis(move)
        restController.notifyObservers
      case _ =>

  /** Do a given function and save it into the UndoManager.
    *
    * @param doThis
    *   A function to do and save into the UndoManager.
    */
  override def doAndPublish(doThis: => PlaygroundInterface): Unit =
    gameNotDone match
      case true =>
        playground = doThis
        restController.notifyObservers
      case _ =>

  /** Reset the GameState to PrepareState() to restart the game. */
  override def restartGame: Unit =
    gamestate.changeState(PrepareState())
    restController.notifyObservers

  /** Checks if the game is still in preparing phase. */
  override def isPreparing: Boolean =
    gamestate.state.equals(PrepareState())

  /** Checks if the game is not over yet. */
  override def gameNotDone: Boolean =
    !(gamestate.state.equals(WinState()) || gamestate.state.equals(TieState()))

  /** Undo the last move. */
  override def undo: PlaygroundInterface = undoManager.undoStep(playground)

  /** Redo the last undone move. */
  override def redo: PlaygroundInterface = undoManager.redoStep(playground)

  /** Insert a chip into the playground.
    *
    * @param move
    *   On which column the chip should be placed on the playground.
    * @return
    *   Return a new playground after the chip was inserted.
    */
  override def insChip(move: Move): PlaygroundInterface = {
    val temp = undoManager.doStep(playground, InsertChipCommand(move))
    checkWinner(temp)
    checkFull(temp)
    temp
  }

  /** Check if the playground is full with chips. If true and game not done, change the GameState to TieState(). If false and game not done,
    * change the GameState to PlayState(). When the game is done, do not change the State.
    */
  override def checkFull(pg: PlaygroundInterface): Unit =
    pg.grid.checkFull() match
      case true =>
        if (gameNotDone)
          gamestate.changeState(TieState())
      case false =>
        if (gameNotDone)
          gamestate.changeState(PlayState())

  /** Check if the playground has a winner. If there is a winner, change the GameState to WinState, else do nothing.
    */
  override def checkWinner(pg: PlaygroundInterface): Unit =
    pg.grid.checkWin() match
      case None =>
      case Some(winningchips) => // 1 == Red, 2 == Yellow
        winnerChips = Some(winningchips)
        gamestate.changeState(WinState())

  /** Gets the color of a chip on a certain coordinate.
    *
    * @param row
    *   The row (y-coordinate) of the playground.
    * @param col
    *   The column (x-coordinate) of the playground.
    * @return
    *   Returns the color chip in that position in ANSII.
    */
  override def getChipColor(row: Int, col: Int): String =
    playground.grid.getCell(row, col).chip.getColorCode

  /** Gets the string of the current state.
    *
    * @return
    *   Returns the string of the current state.
    */
  override def printState: String = gamestate.displayState()

  /** Gets the status string of the playground.
    *
    * @return
    *   Returns the status string of the playground.
    */
  override def playgroundState: String = playground.getStatus()

  /** Prints the playground to a string. */
  override def toString = playground.toString
