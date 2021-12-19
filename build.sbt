name := "bql"

version := "0.1.0"
scalaVersion := "2.13.5"
crossScalaVersions := Seq("2.13.5", "2.12.13")

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:higherKinds",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  //https://stackoverflow.com/questions/56351793
  "-Ywarn-macros:after"
)

libraryDependencies ++= Seq(
  "com.google.cloud" % "google-cloud-bigquery" % "1.137.1",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.4.3",
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)

organization := "net.pishen"
licenses += "Apache-2.0" -> url(
  "https://www.apache.org/licenses/LICENSE-2.0.html"
)
homepage := Some(url("https://github.com/pishen/bql"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/pishen/bql"),
    "scm:git@github.com:pishen/bql.git"
  )
)
developers := List(
  Developer(
    id = "pishen",
    name = "Pishen Tsai",
    email = "",
    url = url("https://github.com/pishen")
  )
)
versionScheme := Some("early-semver")
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "content/repositories/snapshots")
  } else {
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}
