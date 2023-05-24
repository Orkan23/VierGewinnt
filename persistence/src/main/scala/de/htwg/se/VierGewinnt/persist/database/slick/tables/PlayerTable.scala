package de.htwg.se.VierGewinnt.persist.database.slick.tables

import slick.jdbc.PostgresProfile.api.*

class PlayerTable(tag: Tag) extends Table[(String ,Int,String)](tag, "PLAYER") {
  def * = (name,chipId,chipColor)

  def name = column[String]("NAME")
  def chipId = column[Int]("CHIP_ID")
  def chipColor = column[String]("CHIP_COLOR")

  def pk = primaryKey("PK_FIELD", (name, chipId))
}
