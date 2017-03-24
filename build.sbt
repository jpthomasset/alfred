val dependencies = Seq(
  "javax.sip" % "jain-sip-ri" % "1.2.327"
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

lazy val commonSettings = Seq(
  organization := "com.frenchcoder",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.8",
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
)

lazy val root = (project in file("."))
  .settings(
    name := "alfred",
    description := "SIP Virtual Assistant",
    commonSettings,
    libraryDependencies ++= dependencies ++ testDependencies
  )

