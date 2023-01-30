import Dependencies._

ThisBuild / organization := "com.onairentertainment.func_scala_2022"
ThisBuild / scalaVersion := "3.2.0-RC4"
ThisBuild / version := "1.0"
ThisBuild / scalacOptions := Seq(
  "-Ykind-projector:underscores", // turns on the internal kind projector in Scala 3
  "-source:future",
  "-feature",
  "-deprecation"
)

lazy val root = project.in(file(".")).settings(name := "functional-scala-2022", libraryDependencies ++= all)
