name := "geoys"

version := "1.0-SNAPSHOT"

resolvers ++= Seq(
  "semantive-snapshots-internal" at "http://localhost:9080/nexus/content/repositories/snapshots",
  "semantive-releases-internal" at "http://localhost:9080/nexus/content/repositories/releases"
)


libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe.play" %% "play-slick" % "0.5.0.8",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.github.tminglei" % "slick-pg_2.10.1" % "0.1.3.1" exclude("com.vividsolutions", "jts"),
  "org.postgresql" % "postgresql" % "9.2-1003-jdbc4",
  "com.vividsolutions" % "jts" % "1.13-S",
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "commons-lang" % "commons-lang" % "2.6"
)

play.Project.playScalaSettings
