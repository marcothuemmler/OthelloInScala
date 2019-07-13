name          := "Othello In Scala"
organization  := "de.htwg.se"
version       := "1.0"
scalaVersion  := "2.13.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
libraryDependencies += "com.google.inject" % "guice" % "4.2.2"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.5"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"
libraryDependencies += "com.google.inject.extensions" % "guice-assistedinject" % "4.2.2"

coverageExcludedPackages := ".*aview.gui.*"
