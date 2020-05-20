name := "Othello In Scala"
organization := "de.htwg.se"
version := "1.0"
scalaVersion := "2.12.8"

lazy val dependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",

  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "com.google.inject" % "guice" % "4.2.2",
  "net.codingwell" %% "scala-guice" % "4.2.5",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "com.google.inject.extensions" % "guice-assistedinject" % "4.2.2",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.26",
)

libraryDependencies ++= dependencies

lazy val global = project
  .in(file("."))
  .aggregate(UserModule, BoardModule).dependsOn(UserModule, BoardModule)

lazy val UserModule = project
  .settings(
    name := "UserModule",
    libraryDependencies ++= dependencies
  )

lazy val BoardModule = project
  .settings(
    name := "BoardModule",
    libraryDependencies ++= dependencies
  )

coverageExcludedPackages := ".*aview.gui.*"