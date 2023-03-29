/** PlaygroundTemplate base implementation for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.playgroundComponent.playgroundBaseImpl

import de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl.Chip
import de.htwg.se.VierGewinnt.model.playgroundComponent.PlaygroundInterface

import scala.io.AnsiColor.{BLUE_B, RESET}

/** Template to create more playgrounds with different opponents.
 * Player vs Environment (PVE / Computer)
 * Player vs Player (PVP)
 * */
trait PlaygroundTemplate extends PlaygroundInterface {

  /** Returns the size of the playground. */
  override def size = grid.size

  /** Get the position on a specific column, where the most-top chip is placed. */
  override def getDeletePosition(col: Int): Int =
    var i = size - 1
    while (i >= 0 && grid.getCell(i, col).value != Chip.EMPTY) i -= 1
    i += 1
    i

  /** Get the position one higher than the most-top chip. */
  override def getPosition(col: Int): Int =
    col match {
      case col if col > size => 0
      case col if col < 0 => 0
      case _ =>
        var i = size - 1
        while (i >= 0 && grid.getCell(i, col).value != Chip.EMPTY) i -= 1
        i
    }

  /** Build the playgorund together with status, column names, grid and borders. Then return it to a string. */
  override def toString: String =
    val box = getStatus() + "\n" + colnames() + grid + border()
    if (error != "") error else box // print the col is full error if needed

  /** Return the status of the playground Using hte Option-Monade
   * If noone was won yet, return next players turn string.
   * If someone one, return the string of who has won.
   * */
  override def getStatus(): String =
    grid.checkWin() match { //Option-Monade
      case None =>
        val box = "It's your turn " + player(0).getName()
        if (error != "") error else box // print the col is full error if needed
      case Some(num) =>
        if (num._1 == 1) //1 == Red, 2 == Yellow
          val box = "Player Red has won the game!"
          if (error != "") error else box // print the col is full error if needed
        else
          val box = "Player Yellow has won the game!"
          if (error != "") error else box // print the col is full error if needed
    }

  /** Return the column names of the playground. */
  override def colnames(): String = {
    val cols = for {
      n <- 1 to size
    } yield n
    s"${BLUE_B}\t" + cols.mkString("\t") + s"\t ${RESET}\n"
  }

  /** Return the border of the playground. */
  override def border(): String = {
    s"${BLUE_B}  " + ("----" * size) + s"-  ${RESET}\n"
  }
}
