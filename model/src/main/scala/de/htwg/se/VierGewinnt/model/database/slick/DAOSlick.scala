import slick.jdbc.JdbcBackend.Database

import scala.util.Try

object DAOSlick extends DAOInterface[PlaygroundTemplate] {

  val connectIP = sys.env.getOrElse("POSTGRES_IP", "localhost")
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 8080).toString.toInt
  val database_user = sys.env.getOrElse("POSTGRES_USER", "postgres")
  val database_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres")
  val database_name = sys.env.getOrElse("POSTGRES_DB", "postgres")

  val database =
    Database.forURL(
      url = "jdbc:postgresql://" + connectIP + ":" + connectPort + "/" + database_name + "?serverTimezone=UTC",
      user = database_user,
      password = database_pw,
      driver = "org.postgresql.Driver")

  override def create = ???
  //implementation similar to FileIO.scala def save

  override def read = ???
  //implementation similar to FileIO.scala def load

  override def update(input: String) = ???

  override def delete = ???
}
