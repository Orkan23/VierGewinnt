package de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl

import de.htwg.se.VierGewinnt.model
import de.htwg.se.VierGewinnt.model.gridComponent.GridInterface
import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Grid
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import java.lang.IndexOutOfBoundsException
import scala.io.AnsiColor.*

class GridSpec extends AnyWordSpec {
  "Grid keeps the 2D data structure" when {
    "initialized an empty grid without a parameter" should {
      val grid = new Grid()
      "have a size of 7 (default)" in {
        grid should have size 7
      }
      "fill a 2D Vector with empty Chips" in {
        grid.get2DVector() should be(Vector.tabulate(7, 7)((row, col) => Cell()))
      }
      "returns any cell from the grid" in {
        grid.getCell(0, 0) should be(Cell(Chip.EMPTY))
      }
      "do not return anything with negative indices" in {
        an[IndexOutOfBoundsException] should be thrownBy grid.getCell(-1, -1)
      }
      "do not return anything with too high indices" in {
        an[IndexOutOfBoundsException] should be thrownBy grid.getCell(grid.size + 1, 0)
      }
    }
    "initialized an empty grid with custom size" should {
      val grid = new Grid(15)
      "have a size of 7 (default)" in {
        grid should have size 15
      }
      "fill a 2D Vector with empty Chips" in {
        grid.grid should be(Vector.tabulate(15, 15)((row, col) => Cell(Chip.EMPTY)))
      }
      "returns any cell from the grid" in {
        grid.getCell(0, 0) should be(Cell(Chip.EMPTY))
      }
      "do not return anything with negative indices" in {
        an[IndexOutOfBoundsException] should be thrownBy grid.getCell(-1, -1)
      }
      "do not return anything with too high indices" in {
        an[IndexOutOfBoundsException] should be thrownBy grid.getCell(grid.size + 1, 0)
      }
    }
    "replace a Cell" should {
      val grid = new Grid()
      val gridcpy0 = grid.replaceCellRisk(0, 0, Cell(Chip.RED))
      "should change the cell Color from BLUE to RED" in {
        gridcpy0.getCell(0, 0) should be(Cell(Chip.RED))
      }
      val gridcpy1 = grid.replaceCellRisk(0, 0, Cell(Chip.YELLOW))
      "should change the cell Color from RED to YELLOW" in {
        gridcpy1.getCell(0, 0) should be(Cell(Chip.YELLOW))
      }
      "throw an failure" in {
        grid.replaceCell(10, 10, Cell(Chip.EMPTY)).isFailure should be(true)
      }
      "return an success" in {
        grid.replaceCell(0, 0, Cell(Chip.EMPTY)).isSuccess should be(true)
      }
    }
    "removing a cell" should {
      val grid = new Grid()
      "throw an failure" in {
        grid.removeCell(10, 10).isFailure should be(true)
      }
      "return an success" in {
        grid.removeCell(0, 0).isSuccess should be(true)
      }
    }
    "have a string representation" in {
      val grid = new Grid(1)
      grid.toString should be(s"${BLUE_B}  ${BLUE_B}|" + grid.getCell(0, 0) + s"${BLUE_B}|  ${RESET}\n")
    }
    "check correctly if grid is full" should {
      var grid: GridInterface = new Grid(7)
      "if its not full" in {
        grid.checkFull() should be(false)
      }
      "if its full" in {
        for (y <- 0 to (6)) yield { // Height
          for (x <- 0 to (6)) yield { // Width
            grid = grid.replaceCellRisk(y, x, Cell(Chip.RED))
          }
        }
        grid.checkFull() should be(true)
      }
    }
    "winning functions" should {
      var grid = new Grid(7)
      "correctly check success in checkFour() " in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(0, 1, Cell(Chip.RED))
          .replaceCellRisk(0, 2, Cell(Chip.RED))
          .replaceCellRisk(0, 3, Cell(Chip.RED))
        tempgrid.checkFour(0, 0, 0, 1, 0, 2, 0, 3) should be(Some(1,(0,0),(0,1),(0,2),(0,3)))
      }
      "correctly check failure in checkFour() " in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.YELLOW))
          .replaceCellRisk(0, 1, Cell(Chip.RED))
          .replaceCellRisk(0, 2, Cell(Chip.RED))
          .replaceCellRisk(0, 3, Cell(Chip.RED))
        tempgrid.checkFour(0, 0, 0, 1, 0, 2, 0, 3) should be(None)
      }
      "correctly check win in checkHorizontalWin() " in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(0, 1, Cell(Chip.RED))
          .replaceCellRisk(0, 2, Cell(Chip.RED))
          .replaceCellRisk(0, 3, Cell(Chip.RED))
        tempgrid.checkHorizontalWin() should be(Some(1,(0,0),(0,1),(0,2),(0,3)))
      }
      "correctly check win in checkVerticalWin() " in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(1, 0, Cell(Chip.RED))
          .replaceCellRisk(2, 0, Cell(Chip.RED))
          .replaceCellRisk(3, 0, Cell(Chip.RED))
        tempgrid.checkVerticalWin() should be(Some(1,(0,0),(1,0),(2,0),(3,0)))
      }
      "correctly check win in checkDiagonalUpRightWin() " in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(1, 1, Cell(Chip.RED))
          .replaceCellRisk(2, 2, Cell(Chip.RED))
          .replaceCellRisk(3, 3, Cell(Chip.RED))
        tempgrid.checkDiagonalUpRightWin() should be(Some(1,(0,0),(1,1),(2,2),(3,3)))
      }
      "correctly check win in checkDiagonalUpLeftWin() " in {
        val tempgrid = grid
          .replaceCellRisk(4, 0, Cell(Chip.RED))
          .replaceCellRisk(3, 1, Cell(Chip.RED))
          .replaceCellRisk(2, 2, Cell(Chip.RED))
          .replaceCellRisk(1, 3, Cell(Chip.RED))
        tempgrid.checkDiagonalUpLeftWin() should be(Some(1,(4,0),(3,1),(2,2),(1,3)))
      }
      "correctly check result 0 if no win is found" in {
        grid.checkWin() should be(None)
      }
      "correctly check other than 1 on vertical" in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(1, 0, Cell(Chip.RED))
          .replaceCellRisk(2, 0, Cell(Chip.RED))
          .replaceCellRisk(3, 0, Cell(Chip.RED))
        tempgrid.checkWin() should be(Some((1,(0,0),(1,0),(2,0),(3,0))))
      }
      "correctly check other than 1 on diagonal right up" in {
        val tempgrid = grid
          .replaceCellRisk(0, 0, Cell(Chip.RED))
          .replaceCellRisk(1, 1, Cell(Chip.RED))
          .replaceCellRisk(2, 2, Cell(Chip.RED))
          .replaceCellRisk(3, 3, Cell(Chip.RED))
        tempgrid.checkWin() should be(Some((1,(0,0),(1,1),(2,2),(3,3))))
      }
      "correctly check other than 1 on diagonal left up" in {
        val tempgrid = grid
          .replaceCellRisk(4, 0, Cell(Chip.RED))
          .replaceCellRisk(3, 1, Cell(Chip.RED))
          .replaceCellRisk(2, 2, Cell(Chip.RED))
          .replaceCellRisk(1, 3, Cell(Chip.RED))
        tempgrid.checkWin() should be(Some((1,(4,0),(3,1),(2,2),(1,3))))
      }
    }
  }

}
