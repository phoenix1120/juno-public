import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "slippy-map"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.junosw.juno-shared" % "juno-shared_2.9.1" % "1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers := Seq("Local Play Repository" at "file://" + Path.userHome + "/repository/local")
    )

}
