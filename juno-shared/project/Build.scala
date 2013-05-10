import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "juno-shared"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "commons-io" % "commons-io" % "1.4",
      "commons-codec" % "commons-codec" % "1.3",
      "commons-httpclient" % "commons-httpclient" % "3.1",
      "org.apache.httpcomponents" % "httpclient" % "4.1.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers := Seq("Local Play Repository" at "file://" + Path.userHome.absolutePath + "/repository/local"),
      organization := "com.junosw.juno-shared"
    )

}
