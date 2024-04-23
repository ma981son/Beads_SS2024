scalaVersion := "3.4.0"

javacOptions ++= Seq("-source", "11", "-target", "11")

lazy val root = (project in file("."))
  .settings(
    name := "Beads_SA",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      // ScalaFX dependencies
      "org.scalafx" %% "scalafx" % "20.0.0-R31",
      "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
    ),
    coverageEnabled := true
  )
