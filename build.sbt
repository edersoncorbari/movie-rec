name := "movie-rec"
organization := "io.github.edersoncorbari"
version := "0.1"
scalaVersion := "2.11.12"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  val sparkVersion = "2.4.1"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion
  )
}

libraryDependencies ++= {
  val akkaVersion = "2.4.11.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  )
}

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.1"
libraryDependencies += "com.typesafe" % "config" % "1.3.4"
libraryDependencies += "joda-time" % "joda-time" % "2.10.4"
