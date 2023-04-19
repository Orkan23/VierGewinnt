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
  jacocoCoverallsServiceName := "github-actions",
  jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
  jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
  jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN"),
  jacocoReportSettings := JacocoReportSettings(
    "Jacoco Coverage Report",
    None,
    JacocoThresholds(),
    Seq(
      JacocoReportFormats.ScalaHTML,
      JacocoReportFormats.XML
    ),
    "utf-8"
  ),
  jacocoExcludes := Seq(
    "**/GUI.*",
    "*de.htwg.se.VierGewinnt.view.gui.GUI*",
    "**/GuiService.*",
    "**/VG.sc",
    "**/Worksheet.sc"
  )
)

lazy val util = project
  .in(file("util"))
  .settings(
    name := "util",
    description := "Util for Vier Gewinnt",
    commonSettings
  )

lazy val gui = project
  .in(file("gui"))
  .settings(
    name := "gui",
    description := "GUI for Vier Gewinnt",
    commonSettings,
    libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31"
  )
  .dependsOn(core)

lazy val tui = project
  .in(file("tui"))
  .settings(
    name := "tui",
    description := "TUI for Vier Gewinnt",
    commonSettings
  )
  .dependsOn(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "core",
    description := "Core for Vier Gewinnt",
    commonSettings
  )
  .dependsOn(model, persistence, util)

lazy val persistence = project
  .in(file("persistence"))
  .settings(
    name := "persistence",
    description := "Persistence for Vier Gewinnt",
    commonSettings
  )
  .dependsOn(model)

lazy val model = project
  .in(file("model"))
  .settings(
    name := "model",
    description := "Model for Vier Gewinnt",
    commonSettings
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "VierGewinnt",
    commonSettings
  )
  .enablePlugins(JacocoCoverallsPlugin)
  .aggregate(gui, tui, core, util, model, persistence)
