import slick.lifted.Tag
import slick.jdbc.MySQLProfile.api.*

class PlayerTable(tag: Tag) extends Table[(String ,Int,String)](tag, "player") {
  def * = (name,chipId,chipColor)
  def name = column[String]("name")
  def chipId = column[Int]("chipId")
  def chipColor = column[String]("chipColor")

  def pk = primaryKey("pk_field", (name, chipId))
}
