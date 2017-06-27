organization := "com.asidatascience"

name := "dynamic-configuration"

scalaVersion := "2.11.11"

enablePlugins(GitVersioning)

enablePlugins(GitBranchPrompt)

git.useGitDescribe := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.typesafe.play" %% "play-ws" % "2.5.14",
  "ch.qos.logback" % "logback-classic" % "1.2.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.98",
  "org.mockito" % "mockito-core" % "2.7.14" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.play" %% "play" % "2.5.14" % "test",
  "com.typesafe.play" %% "play-test" % "2.5.14" % "test"
)

scalacOptions ++= Seq("-feature", "-deprecation", "-Ywarn-unused-import")

publishMavenStyle := false

s3region := com.amazonaws.services.s3.model.Region.EU_Ireland

s3credentials := new com.amazonaws.auth.DefaultAWSCredentialsProviderChain()

s3acl := com.amazonaws.services.s3.model.CannedAccessControlList.Private

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
   else
    Opts.resolver.sonatypeStaging
  )
