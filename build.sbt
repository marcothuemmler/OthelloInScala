ThisBuild / organization := "de.htwg.se"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.2"
ThisBuild / scalacOptions :=  Seq("-unchecked", "-deprecation")
ThisBuild / trapExit := false

ThisBuild / libraryDependencies := Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.google.inject" % "guice" % "4.2.3",
  "net.codingwell" %% "scala-guice" % "4.2.7",
  "com.typesafe.play" %% "play-json" % "2.9.0",
  "com.google.inject.extensions" % "guice-assistedinject" % "4.2.3",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.6.5",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.7.30",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "mysql" % "mysql-connector-java" % "8.0.20"
)

lazy val OthelloMainModule = project.in(file("."))
  .settings(
    name := "Othello In Scala",
    assemblyJarName in assembly := "Othello.jar",
    test in assembly := {},
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
      "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
    ),
    assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
      case PathList("javax", "servlet", _*)              => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case "application.conf"                            => MergeStrategy.concat
      case "module-info.class"                           => MergeStrategy.concat
      case "CHANGELOG.adoc"                              => MergeStrategy.concat
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
  .aggregate(UserModule, BoardModule).dependsOn(UserModule, BoardModule)

lazy val UserModule = project
  .settings(
    name := "UserModule",
    assemblyJarName in assembly := "UserModule.jar",
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
      case PathList("javax", "servlet", _*)              => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case "application.conf"                            => MergeStrategy.concat
      case "module-info.class"                           => MergeStrategy.concat
      case "CHANGELOG.adoc"                              => MergeStrategy.concat
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

lazy val BoardModule = project
  .settings(
    name := "BoardModule",
    assemblyJarName in assembly := "BoardModule.jar",
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
      case PathList("javax", "servlet", _*)              => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case "application.conf"                            => MergeStrategy.concat
      case "module-info.class"                           => MergeStrategy.concat
      case "CHANGELOG.adoc"                              => MergeStrategy.concat
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

ThisBuild / coverageExcludedPackages := ".*aview.gui.*"
