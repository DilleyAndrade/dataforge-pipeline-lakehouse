ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

lazy val root = (project in file("."))
  .settings(
    name := "dataforge-pipeline-lakehouse",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "3.5.8",
      "com.lihaoyi" %% "requests" % "0.9.3",
      "com.lihaoyi" %% "ujson" % "4.4.3",
      "io.github.cdimascio" % "dotenv-java" % "2.3.2"
    )
  )
