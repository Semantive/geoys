name := "geoys"

scalaVersion := "2.10.3"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-actor" % "2.3-SNAPSHOT",
  "com.typesafe.slick" % "slick_2.10" % "1.0.1",
  "com.github.tminglei" % "slick-pg_2.10.1" % "0.1.3.1",
  "org.postgresql" % "postgresql" % "9.2-1003-jdbc4",
  "com.vividsolutions" % "jts" % "1.13",
  "org.json4s" % "json4s-native_2.10" % "3.2.5"
)

