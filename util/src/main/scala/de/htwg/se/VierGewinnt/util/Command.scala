/** Command trait and UndoManager class for VierGewinnt.
 *
 * @author Victor Gänshirt & Orkan Yücetag */
package de.htwg.se.VierGewinnt.util

trait Command[T]:
  def noStep(t: T): T
  def doStep(t: T): T
  def undoStep(t: T): T
  def redoStep(t: T): T

class UndoManager[T]:
  private var undoStack: List[Command[T]] = Nil
  private var redoStack: List[Command[T]] = Nil
  def doStep(t: T, command: Command[T]): T =
    undoStack = command :: undoStack
    redoStack = Nil // empty redo-stack, incase of inserting a new chip while redo
    command.doStep(t)
  def undoStep(t: T): T =
    undoStack match {
      case Nil => t
      case head :: stack => {
        val result = head.undoStep(t)
        undoStack = stack
        redoStack = head :: redoStack
        result
      }
    }
  def redoStep(t: T): T =
    redoStack match {
      case Nil => t
      case head :: stack => {
        val result = head.redoStep(t)
        redoStack = stack
        undoStack = head :: undoStack
        result
      }
    }
