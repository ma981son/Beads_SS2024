import com.typesafe.sbt.packager.docker.Cmd
import com.typesafe.sbt.packager.docker.DockerChmodType
import sbt.Keys._
import sbt._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._

val scala3Version = "3.4.1"
val AkkaVersion = "2.9.0-M2"
val AkkaHttpVersion = "10.6.0-M1"

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := scala3Version,
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.10" % Test,
    "org.scalafx" %% "scalafx" % "20.0.0-R31",
    "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    "net.codingwell" %% "scala-guice" % "7.0.0",
    "org.playframework" %% "play-json" % "3.0.2",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "com.typesafe.slick" %% "slick" % "3.5.1",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
    "org.postgresql" % "postgresql" % "42.2.20"
  )
)

lazy val dockerSettings = Seq(
  dockerLabels := Map("version" -> version.value),
  dockerExposedVolumes := Seq("/opt/docker"),
  dockerChmodType := DockerChmodType.UserGroupWriteExecute,
  dockerUpdateLatest := true,
  Docker / daemonUserUid := None,
  Docker / daemonUser := "root"
)

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name := "Beads_SA",
    commonSettings,
    coverageEnabled := true
  )
  .dependsOn(
    beads % "compile->compile",
    controller % "compile->compile",
    ui % "compile->compile",
    util % "compile->compile",
    persistence % "compile->compile"
  )
  .aggregate(beads, controller, ui, util, persistence)

lazy val beads = project
  .in(file("./modules/beads"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name := "Beads",
    commonSettings
  )
  .dependsOn(util % "compile->compile")

lazy val controller = project
  .in(file("./modules/beads_controller"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name := "Beads_Controller",
    commonSettings,
    dockerBaseImage := "hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1",
    Compile / mainClass := Some(
      "de.htwg.se.beads_controller.Beads_Controller_Main"
    ),
    dockerExposedPorts := Seq(3000),
    dockerSettings
  )
  .dependsOn(beads % "compile->compile", util % "compile->compile")

lazy val ui = project
  .in(file("./modules/beads_ui"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name := "Beads_UI",
    commonSettings,
    dockerBaseImage := "nicolabeghin/liberica-openjdk-with-javafx-debian:17",
    dockerCommands ++= Seq(
      Cmd("RUN", "apt-get update"),
      Cmd(
        "RUN",
        "apt-get install -y libxrender1 libxtst6 libxi6 libgl1-mesa-glx libgtk-3-0 openjfx libgl1-mesa-dri libgl1-mesa-dev libcanberra-gtk-module libcanberra-gtk3-module default-jdk"
      )
    ),
    Compile / mainClass := Some("de.htwg.se.beads_ui.Beads_UI_Main"),
    dockerSettings
  )
  .dependsOn(controller % "compile->compile", util % "compile->compile")

lazy val util = project
  .in(file("./modules/beads_util"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name := "Beads_Util",
    commonSettings
  )

lazy val persistence = project
  .in(file("./modules/beads_persistence"))
  .enablePlugins(DockerCompose, JavaAppPackaging)
  .settings(
    name := "Beads_Persistence",
    commonSettings,
    dockerBaseImage := "hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1",
    Compile / mainClass := Some(
      "de.htwg.se.beads_persistence.Beads_Persistence_Main"
    ),
    dockerExposedPorts := Seq(3001),
    dockerSettings
  )
  .dependsOn(beads % "compile->compile")
