import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "tile-cache"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "commons-io" % "commons-io" % "1.4",
      "commons-codec" % "commons-codec" % "1.3",
      "commons-httpclient" % "commons-httpclient" % "3.1",
      "commons-logging" % "commons-logging" % "1.1.1",
      "com.junosw.juno-shared" % "juno-shared_2.9.1" % "1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers := Seq("Local Play Repository" at "file://" + Path.userHome + "/repository/local")
    )

}
