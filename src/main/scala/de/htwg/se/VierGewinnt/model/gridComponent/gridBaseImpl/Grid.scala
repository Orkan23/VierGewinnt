/** Grid class base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import scala.io.AnsiColor.BLUE_B
import scala.io.AnsiColor.RESET
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/** Grid with a cell matrix.
 *
 * @param grid Matrix of cells.
 */
case class Grid(grid: Vector[Vector[Cell]]) extends GridInterface :
  def this(size: Int = 7) = this(Vector.tabulate(size, size)((row, col) => Cell())) // call for an empty board

  /** Return the cell on a specific coordinate of the grid.
   *
   * @param row Y-coordinate.
   * @param col X-coordinate.
   * @return Return the found cell.
   */
  override def getCell(row: Int, col: Int): Cell = grid(row)(col)

  /** Return the matrix of the grid. */
  override def get2DVector(): Vector[Vector[Cell]] = grid

  /** Replace a cell with a Try-Monade.
   * If faulty coordinates were given, return a failure, otherwise a success with the new grid. */
  override def replaceCell(row: Int, col: Int, cell: Cell): Try[GridInterface] = {
    val result = Try(copy(grid.updated(row, grid(row).updated(col, cell))))
    result match {
      case Success(v) => Success(v)
      case Failure(e) => Failure(e)
    }
  }

  /** Remove a cell with a Try-Monade.
   * If faulty coordinates were given, return a failure, otherwise a success with the new grid. */
  override def removeCell(row: Int, col: Int): Try[GridInterface] = {
    val result = Try(copy(grid.updated(row, grid(row).updated(col, Cell()))))
    result match {
      case Success(v) => Success(v)
      case Failure(e) => Failure(e)
    }
  }

  /** Replace a cell without a Try-Monade. Easier for testing purposes. */
  override def replaceCellRisk(row: Int, col: Int, cell: Cell): GridInterface = { // For Testing, only if 100% certain
    copy(grid.updated(row, grid(row).updated(col, cell)))
  }

  /** Check if the grid is full, return a boolean. */
  override def checkFull(): Boolean = { // if any of the top rows is not full, return false and stop from checking, if true, grid is completly full
    val result = for {
      i <- 0 until size
      if getCell(0, i).value.getValue == 0
    } yield false
    result.isEmpty
  }

  /** Check if someone has won (4 Chips touch each other horizontally, vertically or diagonally) using Option.
   *
   * @return None won, return none. 1 -> red has won. 2 -> yellow has won. */
  override def checkWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] =
    var result: Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None
    checkHorizontalWin() match {
      case Some(v) => result = Some(v)
      case None => None
    }
    if (result.isEmpty)
      checkVerticalWin() match {
        case Some(v) => result = Some(v)
        case None => None
      }
    if (result.isEmpty)
      checkDiagonalUpRightWin() match {
        case Some(v) => result = Some(v)
        case None => None
      }
    if (result.isEmpty)
      checkDiagonalUpLeftWin() match {
        case Some(v) => result = Some(v)
        case None => None
      }
    result

  /** Check if four chips have the same colour
   *
   * @return (winner, (x0,y0),...,(x3,y4))
   *         winner: 0 -> Noone won. 1 -> red has won. 2 -> yellow has won. */
  override def checkFour(a1: Int, a2: Int, b1: Int, b2: Int, c1: Int, c2: Int, d1: Int, d2: Int): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] =
    val check = getCell(a1, a2).value.getValue
    if (check != 0 && (getCell(b1, b2).value.getValue == check)
      && (getCell(c1, c2).value.getValue == check)
      && (getCell(d1, d2).value.getValue == check)) {
      Some(check, (a1, a2), (b1, b2), (c1, c2), (d1, d2))
    } else {
      None
    }


  /** Iterates through the grid and checks for all horizontal win possibilities.
   *
   * @return (winner, (x0,y0),...,(x3,y4))
   *         winner: 0 -> Noone won. 1 -> red has won. 2 -> yellow has won. */
  override def checkHorizontalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = {
    val result = for {
      y <- 0 until (size -4) //Width
      x <- 0 until size // Height
      win <- checkFour(x, y, x + 1, y, x + 2, y, x + 3, y)
    } yield win
    result.headOption
  }

  /** Iterates through the grid and checks for all vertical win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won. */
  override def checkVerticalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = {
    val result = for {
      x <- 0 to (size - 4) // Width
      y <- 0 until size // Height
      win <- checkFour(x, y, x + 1, y, x + 2, y, x + 3, y)
    } yield win
    result.headOption
}


  /** Iterates through the grid and checks for all diagonal up right win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won. */
  override def checkDiagonalUpRightWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = {
    val result = for {
      y <- 0 to (size - 4) // Height
      x <- 0 to (size - 4) // Width
      win <- checkFour(x, y, x + 1, y + 1, x + 2, y + 2, x + 3, y + 3)
    } yield win
    result.headOption
  }

  /** Iterates through the grid and checks for all diagonal up left win possibilities.
   *
   * @return 0 -> Noone won. 1 -> red has won. 2 -> yellow has won. */
  override def checkDiagonalUpLeftWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = {
    // Check for four cells in a diagonal up-left line
    val result = for {
      y <- 0 to (size - 4)
      x <- 3 until size
      win <- checkFour(x, y, x - 1, y + 1, x - 2, y + 2, x - 3, y + 3)
    } yield win
    result.headOption
  }


  /** Set the size of the grid. */
  size = grid.size

  /** String representation of the grid line-by-line, a 'Cell' calls itselfs toString. */
  override def toString: String = {
    var out = ""
    grid.foreach { (row: Vector[Cell]) =>
      out = out + s"${BLUE_B}  "
      row.foreach { (cell: Cell) =>
        out = out + s"${BLUE_B}|" + cell
      }
      out = out + s"${BLUE_B}|  ${RESET}\n"
    }
    return out
  }
