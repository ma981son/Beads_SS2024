scalaVersion := "3.4.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Beads_SA",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    coverageEnabled := true
  )
