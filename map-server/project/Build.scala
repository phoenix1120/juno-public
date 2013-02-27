import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "map-server"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers := Seq("Local Play Repository" at "file://" + Path.userHome.absolutePath + "/repository/local")
    )

}
