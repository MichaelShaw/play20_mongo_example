import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {
  lazy override val projects = Seq(main)

  val appName         = "blog"
  val appVersion      = "1.0-SNAPSHOT"


  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.0.7",
    "org.fusesource.scalate" % "scalate-core" % "1.5.3"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId"
  )
}
