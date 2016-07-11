organization := "io.github.nthportal"
name := "version-scala"

version := "1.0.0-SNAPSHOT"
isSnapshot := true

scalaVersion := "2.11.8"
scalacOptions += "-target:jvm-1.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

autoAPIMappings := true
