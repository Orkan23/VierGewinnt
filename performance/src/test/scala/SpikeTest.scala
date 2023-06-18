import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class SpikeTest extends Simulation {

  private val httpProtocol = http
    .baseUrl("http://localhost:8081")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("PostmanRuntime/7.32.2")

  private val headers = Map(
    "Cache-Control" -> "no-cache",
    "Content-Type" -> "application/json",
    "Proxy-Connection" -> "keep-alive"
  )

  private val scn = scenario("Spike Test").repeat(10, "i") {
    exec(
      http("Create DB")
        .get("/db/create")
        .headers(headers)
    )
      .exec(
        http("Update DB")
          .post("/db/update")
          .headers(headers)
          .body(RawFileBody("src/test/resources/game.json"))
      )
      .exec(
        http("Read DB")
          .get("/db/read")
          .headers(headers)
      )
      .exec(
        http("Delete DB")
          .get("/db/delete")
          .headers(headers)
      )
  }
  setUp(scn.inject(atOnceUsers(10000))).protocols(httpProtocol)
}
