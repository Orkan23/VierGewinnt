/** State Interface for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.util

trait State[T] {
  def displayState: String
}
