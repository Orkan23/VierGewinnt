/** FileIO JSON Implementation for VierGewinnt.
  *
  * @author
  *   Victor Gänshirt & Orkan Yücetag
  */
package de.htwg.se.VierGewinnt.persist.fileio.fileIoJsonImpl

import de.htwg.se.VierGewinnt.persist.fileio.FileIOInterface
import java.io.PrintWriter
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.io.Source
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/** FileIO JSON Implementation, to save and load the state of a game with a JSON file. */
class FileIO extends FileIOInterface {

  /** Load the game from a "playground.json" file and return the playground. */
  override def load: Try[String] =
    Try {
      Source.fromFile("game.json").getLines().mkString
    }

  /** Save the game to a "playground.json" file.
    *
    * @param playground
    *   The playground to save.
    */
  def save(playground: String): Unit =
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(playground)
    pw.close()
}
