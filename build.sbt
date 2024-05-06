val scala3Version = "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "Beads_SA",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      "org.scalafx" %% "scalafx" % "20.0.0-R31",
      "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      "net.codingwell" %% "scala-guice" % "7.0.0",
      "org.playframework" %% "play-json" % "3.0.2"
    ),
    coverageEnabled := true
  )
  .dependsOn(beads, controller, ui, util)
  .aggregate(beads, controller, ui, util)

lazy val beads = project
  .in(file("./modules/beads"))
  .settings(
    name := "Beads",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      "org.scalafx" %% "scalafx" % "20.0.0-R31",
      "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      "net.codingwell" %% "scala-guice" % "7.0.0",
      "org.playframework" %% "play-json" % "3.0.2"
    )
  )
  .dependsOn(util)

lazy val controller = project
  .in(file("./modules/beads_controller"))
  .settings(
    name := "Beads_Controller",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test"
    )
  )
  .dependsOn(beads, util)

lazy val ui = project
  .in(file("./modules/beads_ui"))
  .settings(
    name := "Beads_UI",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      "org.scalafx" %% "scalafx" % "20.0.0-R31"
    )
  )
  .dependsOn(controller, util)

lazy val util = project
  .in(file("./modules/beads_util"))
  .settings(
    name := "Beads_Util",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      "org.scalafx" %% "scalafx" % "20.0.0-R31"
    )
  )
