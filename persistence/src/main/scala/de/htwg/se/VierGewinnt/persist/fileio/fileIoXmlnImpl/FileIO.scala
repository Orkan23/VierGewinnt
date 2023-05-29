/** FileIO XML Implementation for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */
package de.htwg.se.VierGewinnt.persist.fileio.fileIoXmlnImpl

import com.google.inject.Guice
import de.htwg.se.VierGewinnt.persist.fileio.FileIOInterface

import java.io.File
import java.io.PrintWriter
import scala.util.{Failure, Success, Try}
import scala.xml.PrettyPrinter

/** FileIO XML Implementation, to save and load the state of a game with a XML file. */
class FileIO extends FileIOInterface {

  /** Load the game from a "playground.xml" file and return the playground. */
  override def load: Try[String] =
    Try {
      val file = scala.xml.XML.loadFile("playground.xml")
      file.toString()
    }

  /** Save the game to a "playground.json" file.
    *
    * @param playground
    *   The playground to save.
    */

  /** Call the saveString function to save the game to a "playground.xml" file.
    *
    * @param playground
    *   The playground to save.
    */
  override def save(playground: String): Unit =
    val pw = new PrintWriter(new File("playground.xml"))
    pw.write(playground)
    pw.close()

}
