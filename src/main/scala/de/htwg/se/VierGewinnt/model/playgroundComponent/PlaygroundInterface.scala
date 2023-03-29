/** Playground Interface for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playgroundComponent

import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.playerComponent.PlayerInterface

/** Interface for the playground. */
trait PlaygroundInterface:
  /** Returns the grid of the playground. */
  def grid: GridInterface

  /** Returns the playerlist of the playground. */
  def player: List[PlayerInterface]

  /** Returns the size of the playground. */
  def size: Int

  /** Error string to communicate status messages. */
  var error: String = ""

  /** Insert a Chip onto a specific column and return the new playground. */
  def insertChip(col: Int): PlaygroundInterface

  /** A automatic process that inserts a Chip onto a specific column and return the new playground. */
  def computerInsertChip(): PlaygroundInterface

  /** Take away a chip from a specific column and return the new playground. */
  def takeAwayChip(col: Int): PlaygroundInterface

  /** Get the position on a specific column, where the most-top chip is placed. */
  def getDeletePosition(col: Int): Int

  /** Get the position one higher than the most-top chip. */
  def getPosition(col: Int): Int

  /** Return the status of the playground. */
  def getStatus(): String

  /** Return the column names of the playground. */
  def colnames(): String

  /** Return the border of the playground. */
  def border(): String
