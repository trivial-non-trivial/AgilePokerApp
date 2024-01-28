import scala.collection.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

// enable the Scala.js plugin that’s in 'project/plugins.sbt'
enablePlugins(ScalaJSPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "AgilePokerPublic"
  )

libraryDependencies ++= Seq(
  "io.laminext" %%% "websocket" % "0.16.2",
  "io.laminext" %%% "websocket-circe" % "0.16.2",
  "io.laminext" %%% "fetch-circe" % "0.16.2"
)