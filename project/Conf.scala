import sbt._

object Conf {
  lazy val dbConf = settingKey[(String, String, String)]("Typesafe config file with slick settings")
}
