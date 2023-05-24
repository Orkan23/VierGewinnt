package de.htwg.se.VierGewinnt.persist.database.slick

import ch.qos.logback.classic.Logger
import de.htwg.se.VierGewinnt.persist.database.slick.tables.PlayerTable
import de.htwg.se.VierGewinnt.persist.database.DAOInterface
import de.htwg.se.VierGewinnt.persist.fileio.service.PersistenceRestService.getClass
import org.slf4j.LoggerFactory
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Failure
import scala.util.Success
import scala.Console.RED_B
import scala.Console.YELLOW_B
import slick.jdbc.meta.MTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

object DAOSlickPlayer extends DAOInterface {

  val connectIP = sys.env.getOrElse("POSTGRES_IP", "viergewinnt-db-1")
  val connectPort = sys.env.getOrElse("POSTGRES_PORT", 5432).toString.toInt
  val database_user = sys.env.getOrElse("POSTGRES_USER", "postgres")
  val database_pw = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres")
  val database_name = sys.env.getOrElse("POSTGRES_DB", "viergewinnt")

  val db =
    Database.forURL(
      url = s"jdbc:postgresql://${connectIP}:${connectPort}/${database_name}?serverTimezone=UTC",
      user = database_user,
      password = database_pw,
      driver = "org.postgresql.Driver"
    )

  val playerTable = new TableQuery(new PlayerTable(_))

  val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList

  override def create(): Unit =
    Await.result(
      db.run(
        DBIO.seq(
          MTable.getTables map (tables => {
            if (!tables.exists(_.name.name == playerTable.baseTableRow.tableName))
              db.run(playerTable.schema.create)
            else
              println("Table \"PLAYER\" is already existing")
          })
        )
      ),
      Duration.Inf
    )

  override def read(): String =
    val query = playerTable.result
    val result = db.run(query)
    val rows = Await.result(result, Duration.Inf)

    val formattedRows = rows.map { case (id, name, chipId, chipColor) =>
      s"  ${id}\t${name}\t${chipId}\t${chipColor}"
    }
    formattedRows.mkString("\n")

  override def update(input: String): Unit =
    val json: JsValue = Json.parse(input)

    val playersNumbers = Seq(1, 2)
    playersNumbers.map(nr =>
      val player = (json \ "playground" \ s"player${nr}").get.toString().replaceAll("\"", "")
      val splitPlayer = player.split("&")
      val name: String = splitPlayer(0)
      val chip: (Int, String) = if (splitPlayer(1) == "RED") then (1, "RED_B") else (2, "YELLOW_B")

      val insertAction = playerTable.insertOrUpdate(nr, name, chip._1, chip._2)
      val insertResult = db.run(insertAction)
      Await.result(insertResult, Duration.Inf)
    )

  override def delete(): Unit =
    val action = playerTable.delete
    Await.result(db.run(action), Duration.Inf)
}
