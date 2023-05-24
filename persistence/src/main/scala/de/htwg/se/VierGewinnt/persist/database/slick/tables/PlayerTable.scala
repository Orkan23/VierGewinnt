package de.htwg.se.VierGewinnt.persist.database.slick.tables

import slick.jdbc.PostgresProfile.api.*

class PlayerTable(tag: Tag) extends Table[(Int, String, Int, String)](tag, "PLAYER") {
  def * = (id, name, chipId, chipColor)

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def chipId = column[Int]("CHIP_ID")
  def chipColor = column[String]("CHIP_COLOR")
}
