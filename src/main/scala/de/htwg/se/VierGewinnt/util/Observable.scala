/** Observable class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.util

trait Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer) = subscribers = subscribers :+ s

  def remove(s: Observer) = subscribers = subscribers.filterNot(o => o == s)

  def notifyObservers = subscribers.foreach(o => o.update)
}
