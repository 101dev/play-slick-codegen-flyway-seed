import com.typesafe.config.ConfigFactory

name := """play-slick-codegen-flyway-seed"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)
libraryDependencies += "com.h2database" % "h2" % "1.4.188"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "1.0.1"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

Conf.dbConf := {
  val cfg = ConfigFactory.parseFile((resourceDirectory in Compile).value / "application.conf")
  val prefix = "slick.dbs.default"
  (cfg.getString(s"$prefix.url"), cfg.getString(s"$prefix.user"), cfg.getString(s"$prefix.password"))
}
