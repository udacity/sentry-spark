import sbt._
import Keys._

val sparkVersion = "2.4.5"
val sentryVersion = "1.7.30"

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala211, scala212)

lazy val artifactoryUser: String = sys.props.getOrElse("ARTIFACTORY_USERNAME", "")
lazy val artifactoryPassword: String = sys.props.getOrElse("ARTIFACTORY_PASSWORD", "")

lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "com.udacity.data",
  version := "0.0.1-alpha05-SNAPSHOT",
  crossScalaVersions := supportedScalaVersions,
  scalacOptions ++= Seq("-target:jvm-1.8",
                        "-deprecation",
                        "-feature",
                        "-unchecked"),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers ++= Seq(
    "snapshots" at "https://udacity.jfrog.io/udacity/libs-snapshot-local",
    DefaultMavenRepository,
    Resolver.defaultLocal,
    Resolver.mavenLocal),
  publishTo := Some("snapshots" at "https://udacity.jfrog.io/udacity/libs-snapshot-local"),
  credentials += Credentials("Artifactory Realm", "udacity.jfrog.io", artifactoryUser, artifactoryPassword),
  scalafmtOnCompile := true
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "io.sentry" % "sentry" % sentryVersion,
  // Testing
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
)

lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    name := "sentry-spark",
    description := "Sentry Integration for Apache Spark",
    run / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    parallelExecution in Test := false,
    fork in Test := true,
    javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
      "io.sentry" % "sentry" % sentryVersion,
      // Testing
      "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    )
  )