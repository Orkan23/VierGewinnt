package de.htwg.se.VierGewinnt.persist.database.slick

import de.htwg.se.VierGewinnt.persist.database.DAOInterface

class DAOSlickImpl extends DAOInterface {

  val daoPlayer = DAOSlickPlayer
  //val daoPlayground = DAOSlickPlayground

  override def create: Unit =
    daoPlayer.create
  // daoPlayground.create()

  override def read: String =
    daoPlayer.read
  // daoPlayground.read()

  override def update(input: String): Unit =
    daoPlayer.update(input)
  // daoPlayground.update()

  override def delete: Unit =
    daoPlayer.delete
  // daoPlayground.delete()
}
