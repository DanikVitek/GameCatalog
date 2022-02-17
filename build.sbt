ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "3.1.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.36"
libraryDependencies += "org.jetbrains" % "annotations" % "23.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "GameCatalog",
    idePackagePrefix := Some("edu.mmsa.danikvitek")
  )
