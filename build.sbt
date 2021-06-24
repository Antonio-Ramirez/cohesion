name := "cohesion"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.typesafe"       %  "config"               % "1.3.0",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
  "ch.qos.logback"     %  "logback-classic"      % "1.2.3",
  "org.postgresql"     %  "postgresql"           % "42.2.14",
  "com.typesafe.slick" %% "slick"                % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp"       % "3.3.2",
  "com.typesafe.akka"  %% "akka-stream-kafka"    % "2.1.0",
  "com.typesafe.akka"  %% "akka-stream"          % "2.6.14",
  "io.circe"           %% "circe-core"           % "0.13.0",
  "io.circe"           %% "circe-generic"        % "0.13.0",
  "io.circe"           %% "circe-parser"         % "0.13.0",
  "com.typesafe.akka"  %% "akka-stream-testkit"  % "2.6.14"     % "test",
  "org.scalamock"      %% "scalamock"            % "5.1.0"      % "test",
  "org.scalatest"      %% "scalatest"            % "3.2.9"      % "it, test",
  "io.zonky.test"      %  "embedded-postgres"    % "1.2.6"      % "it, test"
)

mainClass in (Compile, run) := Some("com.cohesion.challenge.ApplicationServer")

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    Defaults.itSettings
  )