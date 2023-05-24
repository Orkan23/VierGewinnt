package de.htwg.se.VierGewinnt.persist.database.slick

import de.htwg.se.VierGewinnt.persist.database.slick.tables.PlayerTable
import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

object DAOSlickPlayground extends DAOInterface {

  val connectIP = sys.env.getOrElse("POSTGRES_IP", "localhost")
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 5432).toString.toInt
  val database_user = sys.env.getOrElse("POSTGRES_USER", "postgres")
  val database_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres")
  val database_name = sys.env.getOrElse("POSTGRES_DB", "viergewinnt")

  val database =
    Database.forURL(
      url = "jdbc:postgresql://" + connectIP + ":" + connectPort + "/" + database_name + "?serverTimezone=UTC",
      user = database_user,
      password = database_pw,
      driver = "org.postgresql.Driver"
    )

  override def create(): Unit = ???

  override def read(): String = ???

  override def update(input: String): Unit = ???

  override def delete(): Unit = ???
}
