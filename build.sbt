scalaVersion := "3.4.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Beads_SA",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
