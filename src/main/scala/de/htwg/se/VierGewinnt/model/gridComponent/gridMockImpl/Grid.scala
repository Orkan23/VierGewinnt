/** Grid mock implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.gridComponent.gridMockImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import scala.util.Success
import scala.util.Try

class Grid(grid: Vector[Vector[Cell]]) extends GridInterface {
  override def getCell(row: Int, col: Int): Cell = grid(row)(col)

  override def get2DVector(): Vector[Vector[Cell]] = grid

  override def removeCell(row: Int, col: Int): Try[GridInterface] = Success[GridInterface](this)

  override def replaceCell(row: Int, col: Int, cell: Cell): Try[GridInterface] = Success[GridInterface](this)

  override def replaceCellRisk(row: Int, col: Int, cell: Cell): GridInterface = this

  override def checkFull(): Boolean = true

  override def checkWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = Some((1,(0,0),(0,1),(0,2),(0,3)))

  override def checkFour(a1: Int, a2: Int, b1: Int, b2: Int, c1: Int, c2: Int, d1: Int, d2: Int): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None

  override def checkHorizontalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None

  override def checkVerticalWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None

  override def checkDiagonalUpRightWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None

  override def checkDiagonalUpLeftWin(): Option[(Int, (Int, Int), (Int, Int), (Int, Int), (Int, Int))] = None
}
