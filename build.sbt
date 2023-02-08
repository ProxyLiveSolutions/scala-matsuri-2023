import Dependencies._

ThisBuild / organization := "com.onairentertainment.scala_matsuri_2023"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / version := "1.0"
ThisBuild / scalacOptions := Seq(
  "-Ykind-projector:underscores", // turns on the internal kind projector in Scala 3
  "-source:future",
  "-feature",
  "-deprecation"
)

lazy val root = project.in(file(".")).settings(name := "composable-gens", libraryDependencies ++= all)
