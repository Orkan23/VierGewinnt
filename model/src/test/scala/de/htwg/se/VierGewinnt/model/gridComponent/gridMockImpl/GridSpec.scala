package de.htwg.se.VierGewinnt.model.gridComponent.gridMockImpl

import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.VierGewinnt.model.gridComponent.gridMockImpl.Grid
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Success

class GridSpec extends AnyWordSpec {
  "A Grid Mock Implementation" when {
    "initialized" should {
      val GridObject = new Grid(Vector.tabulate(7, 7)((row, col) => Cell()))
      GridObject.size = 7
      "should have the size 7" in {
        GridObject.size should be (7)
      }
      "should return an empty cell in getCell()" in {
        GridObject.getCell(0,0) should be (Cell())
      }
      "should return the 2D Vector in get2DVector" in {
        GridObject.get2DVector() should be (Vector.tabulate(7, 7)((row, col) => Cell()))
      }
      "should remove a cell in removeCell" in {
        GridObject.removeCell(0,0) should be (Success[GridInterface](GridObject))
      }
      "should replace a cell in replaceCell" in {
        GridObject.replaceCell(0,0, Cell()) should be (Success[GridInterface](GridObject))
      }
      "should replace a cell risky in replaceCellRisk" in {
        GridObject.replaceCellRisk(0,0, Cell()) should be (GridObject)
      }
      "should check if full true in checkFull" in {
        GridObject.checkFull() should be (true)
      }
      "should check if who won in checkWin" in {
        GridObject.checkWin() should be (Some((1,(0,0),(0,1),(0,2),(0,3))))
      }
      "should check four Cells in a row in checkFour" in {
        GridObject.checkFour(0,0,0,0,0,0,0,0) should be (None)
      }
      "should check horizontal win" in {
        GridObject.checkHorizontalWin() should be (None)
      }
      "should check vertical win" in {
        GridObject.checkVerticalWin() should be (None)
      }
      "should check diagonal up left win" in {
        GridObject.checkDiagonalUpLeftWin() should be (None)
      }
      "should check diagonal up right win in" in {
        GridObject.checkDiagonalUpRightWin() should be (None)
      }
    }
  }
}
