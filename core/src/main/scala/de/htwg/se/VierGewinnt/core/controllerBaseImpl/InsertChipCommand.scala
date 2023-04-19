/** InsertChipCommand class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.core.controllerBaseImpl

import de.htwg.se.VierGewinnt.model.playgroundComponent.{PlaygroundInterface, playgroundBaseImpl}
import de.htwg.se.VierGewinnt.util.{Command, Move}

/** InsertChipCommand class to work with the Command-Pattern and UndoManager.
 *
 * @param move A move to do.
 */
class InsertChipCommand(move: Move) extends Command[PlaygroundInterface]:
  /** Do not do a step. Returns the same playground. */
  override def noStep(playground: PlaygroundInterface): PlaygroundInterface = playground

  /** Do a step. Returns the new playground with the playground inserting and computer inserting function. */
  override def doStep(playground: PlaygroundInterface): PlaygroundInterface = playground.insertChip(move.col).computerInsertChip()

  /** Undo a step. Take away the last placed chip. */
  override def undoStep(playground: PlaygroundInterface): PlaygroundInterface = playground.takeAwayChip(move.col)

  /** Redo a step. Place the last taken chip on its original position. */
  override def redoStep(playground: PlaygroundInterface): PlaygroundInterface = playground.insertChip(move.col)
