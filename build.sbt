ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "3.1.1"

val unitTesting = Seq(
    "org.scalactic" %% "scalactic" % "3.2.12",
    "org.scalatest" %% "scalatest" % "3.2.12" % "test"
)

val logging = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.11",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

val jbAnnotations = "org.jetbrains" % "annotations" % "23.0.0"

libraryDependencies ++= unitTesting
libraryDependencies ++= logging

libraryDependencies += jbAnnotations

libraryDependencies += "org.mariadb.jdbc" % "mariadb-java-client" % "3.0.4"

libraryDependencies += "com.github.losizm" %% "little-json" % "9.0.0"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "4.0.1" % "provided"
enablePlugins(TomcatPlugin)
containerPort := 8082

lazy val root = (project in file("."))
  .settings(
    name := "GameCatalog",
    idePackagePrefix := Some("edu.mmsa.danikvitek.gamecatalog")
  )
