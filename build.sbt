val scala3Version = "3.2.2"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := scala3Version,
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  libraryDependencies += "com.google.inject" % "guice" % "5.1.0",
  libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.1.1").cross(CrossVersion.for3Use2_13),
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
  libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC7"),
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "3.0.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4"
  ),
    coverageEnabled := true
)


lazy val util = project
  .in(file("util"))
  .settings(name := "util", description := "Util for Vier Gewinnt", commonSettings, coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*")

lazy val gui = project
  .in(file("gui"))
  .settings(
    name := "gui",
    description := "GUI for Vier Gewinnt",
    commonSettings,
    libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31",
    coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*",
  )
  .dependsOn(core)

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
    coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*"
  )
  .dependsOn(model, persistence, util)

lazy val persistence = project
  .in(file("persistence"))
  .settings(
    name := "persistence",
    description := "Persistence for Vier Gewinnt",
    commonSettings,
    coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*"
  )
  .dependsOn(model)

lazy val model = project
  .in(file("model"))
  .settings(
    name := "model",
    description := "Model for Vier Gewinnt",
    commonSettings,
    coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*"
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "VierGewinnt",
    commonSettings,
    coverageExcludedPackages := "<empty>;.*GUI.*;.*gui\\.*"
  )
  .aggregate(gui, tui, core, util, model, persistence)
