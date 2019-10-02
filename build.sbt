name := "movie-rec"
organization := "io.github.edersoncorbari"
version := "0.1"
scalaVersion := "2.11.12"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

coverageMinimum := 50
coverageFailOnMinimum := false
coverageHighlighting := true

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
logLevel := Level.Error

// Spark
libraryDependencies ++= {
  val sparkVersion = "2.4.1"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion
  )
}

// Akka
libraryDependencies ++= {
  val akkaVersion = "2.4.11.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  )
}

// Utilities
libraryDependencies ++= {
  Seq(
    "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.1",
    "com.typesafe" % "config" % "1.3.4",
    "org.scalatest" %% "scalatest" % "3.0.7" % Test,
    "joda-time" % "joda-time" % "2.10.4"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
test in assembly := {}
assemblyJarName in assembly := "movie-rec.jar"