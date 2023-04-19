val scala3Version = "3.2.2"

lazy val commonSettings = Seq(
  version := "0.2.0-SNAPSHOT",
  scalaVersion := scala3Version,
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  libraryDependencies += "com.google.inject" % "guice" % "5.1.0",
  libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.1.1").cross(CrossVersion.for3Use2_13),
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
  libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.9.2").cross(CrossVersion.for3Use2_13),
)

lazy val gui = project
  .in(file("gui"))
  .settings(
    name := "gui",
    description := "GUI for Vier Gewinnt",
    commonSettings,
    libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"
  ).dependsOn(core)

lazy val tui = project
  .in(file("tui"))
  .settings(
    name := "tui",
    description := "TUI for Vier Gewinnt",
    commonSettings
  ).dependsOn(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "core",
    description := "Core for Vier Gewinnt",
    commonSettings
  ).dependsOn(root)

lazy val root = project
  .in(file("."))
  .settings(
    name := "VierGewinnt",
    commonSettings,
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN"),
    //jacocoExcludes in Test := Seq(
    //  "de.htwg.se.VierGewinnt.VierGewinnt*",
    //  "de.htwg.se.VierGewinnt.aview.GUI*"
    //),
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      lazy val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux")   => "linux"
        case n if n.startsWith("Mac")     => "mac"
        case n if n.startsWith("Windows") => "win"
        case _                            => throw new Exception("Unknown platform!")
      }

      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
    },
    publishArtifact := false
  )
  .enablePlugins(JacocoCoverallsPlugin)
