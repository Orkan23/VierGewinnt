/** Cell class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.model.gridComponent.gridBaseImpl

/** Define a Cell to be used in the grid. */
case class Cell(value: Chip = Chip.EMPTY) {

  /** Store the value of the chip inside of the cell. */
  val chip: Chip = value

  /** Returns the color code of the chip. */
  override def toString: String = s"${value.getColorCode}" + s"   "
}
