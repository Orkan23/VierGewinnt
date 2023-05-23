import com.typesafe.sbt.packager.docker.ExecCmd

val scala3Version = "3.2.2"

val akkaVersion = "2.8.0"
val akkaHttp = "10.5.0"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := scala3Version,
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.2.15",
    "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    "com.google.inject" % "guice" % "5.1.0",
    ("net.codingwell" %% "scala-guice" % "5.1.1"),
    "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
    "com.typesafe.play" %% "play-json" % "2.10.0-RC7",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "com.typesafe.akka" %% "akka-http-core" % akkaHttp,
    "ch.qos.logback" % "logback-classic" % "1.4.6",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    ("com.typesafe.slick" %% "slick" % "3.5.0-M3").cross(CrossVersion.for3Use2_13),
    "org.slf4j" % "slf4j-nop" % "1.6.4"
  ),
  dockerBaseImage := "sbtscala/scala-sbt:eclipse-temurin-jammy-17.0.5_8_1.8.3_3.2.2",
  Docker / daemonUserUid := None,
  Docker / daemonUser := "root"
)


lazy val util = project
  .in(file("util"))
  .settings(
    name := "util",
    description := "Util for Vier Gewinnt",
    commonSettings,
    Compile / run / mainClass := Some("de.htwg.se.VierGewinnt.util.service.UtilRestService"),
    Compile / compile / mainClass := Some("de.htwg.se.VierGewinnt.util.service.UtilRestService"),
    dockerExposedPorts := Seq(8083)
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val gui = project
  .in(file("gui"))
  .settings(
    name := "gui",
    description := "GUI for Vier Gewinnt",
    commonSettings,
    libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31",
    logLevel := sbt.util.Level.Info,
    dockerCommands ++= Seq(
      ExecCmd("RUN", "apt-get", "update"),
      ExecCmd("RUN", "apt-get", "install", "-y", "openjfx", "dbus-x11", "csh", "libgl1", "libx11-6"),
      ExecCmd("CMD", "sbt", "run")
    )
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val tui = project
  .in(file("tui"))
  .settings(name := "tui", description := "TUI for Vier Gewinnt", commonSettings)
  .dependsOn(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "core",
    description := "Core for Vier Gewinnt",
    commonSettings,
    dockerExposedPorts := Seq(8080),
    logLevel := sbt.util.Level.Info
  )
  .dependsOn(model, util)
  .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val persistence = project
  .in(file("persistence"))
  .settings(
    name := "persistence",
    description := "Persistence for Vier Gewinnt",
    commonSettings,
    dockerExposedPorts ++= Seq(8081),
    logLevel := sbt.util.Level.Info
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val model = project
  .in(file("model"))
  .settings(
    name := "model",
    description := "Model for Vier Gewinnt",
    commonSettings,
    dockerExposedPorts ++= Seq(8082),
    logLevel := sbt.util.Level.Info
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "VierGewinnt",
    commonSettings
  )
  .aggregate(gui, tui, core, util, model, persistence)
