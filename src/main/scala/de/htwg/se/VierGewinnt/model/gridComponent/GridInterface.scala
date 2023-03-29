/** Grid Interface for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.gridComponent

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.{Cell, Chip}

import scala.io.AnsiColor.{BLUE_B, RESET}
import scala.util.{Failure, Success, Try}

/** This class keeps the data of the game as a 2D Vector auxilary constructor gets called for an empty board. The default constructor is to
  * copy&replace
  */
trait GridInterface:

  /** Return the cell on a specific coordinate of the grid.
   *
   * @param row Y-coordinate.
   * @param col X-coordinate.
   * @return Return the found cell.
   */
  def getCell(row: Int, col: Int): Cell

  /** Return the matrix of the grid. */
  def get2DVector(): Vector[Vector[Cell]]

  /** Replace a cell with a Try-Monade.
   * If faulty coordinates were given, return a failure, otherwise a success with the new grid. */
  def replaceCell(row: Int, col: Int, cell: Cell): Try[GridInterface]

  /** Remove a cell with a Try-Monade.
   * If faulty coordinates were given, return a failure, otherwise a success with the new grid. */
  def removeCell(row: Int, col: Int): Try[GridInterface]

  /** Replace a cell without a Try-Monade. Easier for testing purposes. */
  def replaceCellRisk(row: Int, col: Int, cell: Cell): GridInterface

  /** Check if the grid is full, return a boolean. */
  def checkFull(): Boolean

  /** Check if someone has won (4 Chips touch each other horizontally, vertically or diagonally) using Option.
   *
   * @return Noone won, return none. 1 -> red has won. 2 -> yellow has won.*/
  def checkWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** Check if four chips have the same colour
   *
   * @return */
  def checkFour(a1: Int, a2: Int, b1: Int, b2: Int, c1: Int, c2: Int, d1: Int, d2: Int): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** Iterates through the grid and checks for all horizontal win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won.*/
  def checkHorizontalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** Iterates through the grid and checks for all vertical win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won.*/
  def checkVerticalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** Iterates through the grid and checks for all diagonal up right win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won.*/
  def checkDiagonalUpRightWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** Iterates through the grid and checks for all diagonal up left win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won.*/
  def checkDiagonalUpLeftWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))]

  /** The size of the grid. */
  var size: Int = 0
