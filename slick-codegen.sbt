import slick.codegen.SourceCodeGenerator

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

val genTables: TaskKey[Seq[File]] = taskKey[Seq[File]]("Generate table definitions from database")

genTables := {
  val outputDir = sourceManaged.value / "slick"
  val pkg = "model"
  val (url, user, pwd) = Conf.dbConf.value
  val jdbcDriver = "org.h2.Driver"
  val slickDriver = "slick.driver.H2Driver"
  val driver = slick.driver.H2Driver
  val dbFactory = driver.api.Database
  val db = dbFactory.forURL(url, driver = jdbcDriver, user = user, password = pwd)
  val excludedTables = Seq("schema_version")
  try {
    val tables =
      driver.defaultTables
        .map(_.filterNot(excludedTables contains _.name.name))
    val modelAction = driver.createModel(Some(tables), ignoreInvalidDefaults = false).withPinnedSession
    val model = Await.result(db.run(modelAction), Duration.Inf)
    val generator = new SourceCodeGenerator(model)
    generator.writeToFile(slickDriver, outputDir.getPath, pkg)
  } finally db.close()
  Seq[File](outputDir / pkg / "Tables.scala")
}
