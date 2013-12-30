name := "geoys"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe.play" %% "play-slick" % "0.5.0.8",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.github.tminglei" % "slick-pg_2.10.1" % "0.1.3.1",
  "org.postgresql" % "postgresql" % "9.2-1003-jdbc4",
  "com.vividsolutions" % "jts" % "1.13"
)     

play.Project.playScalaSettings
