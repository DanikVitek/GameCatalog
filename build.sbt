ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "3.1.1"

val unitTesting = Seq(
    "org.scalactic" %% "scalactic" % "3.2.11",
    "org.scalatest" %% "scalatest" % "3.2.11" % "test"
)

val logging = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.10",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

val jbAnnotations = "org.jetbrains" % "annotations" % "23.0.0"

libraryDependencies ++= unitTesting
libraryDependencies ++= logging

libraryDependencies += jbAnnotations

libraryDependencies += "org.mariadb.jdbc" % "mariadb-java-client" % "3.0.3"

lazy val root = (project in file("."))
  .settings(
    name := "GameCatalog",
    idePackagePrefix := Some("edu.mmsa.danikvitek.gamecatalog")
  )
