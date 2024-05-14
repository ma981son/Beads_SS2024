val scala3Version = "3.4.1"
val AkkaVersion = "2.9.0-M2"
val AkkaHttpVersion = "10.6.0-M1"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := scala3Version,
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    "org.scalafx" %% "scalafx" % "20.0.0-R31",
    "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    "net.codingwell" %% "scala-guice" % "7.0.0",
    "org.playframework" %% "play-json" % "3.0.2",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "Beads_SA",
    commonSettings,
    coverageEnabled := true
  )
  .dependsOn(beads, controller, ui, util)
  .aggregate(beads, controller, ui, util)

lazy val beads = project
  .in(file("./modules/beads"))
  .settings(
    name := "Beads",
    commonSettings
  )
  .dependsOn(util)

lazy val controller = project
  .in(file("./modules/beads_controller"))
  .settings(
    name := "Beads_Controller",
    commonSettings
  )
  .dependsOn(beads, util)

lazy val ui = project
  .in(file("./modules/beads_ui"))
  .settings(
    name := "Beads_UI",
    commonSettings
  )
  .dependsOn(controller, util)

lazy val util = project
  .in(file("./modules/beads_util"))
  .settings(
    name := "Beads_Util",
    commonSettings
  )
