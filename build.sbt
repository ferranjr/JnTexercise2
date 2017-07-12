name := "exercise2"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.typelevel"   %%  "cats"          % "0.9.0",
  "org.scalatest"   %%  "scalatest"     % "3.0.1"   % Test,
  "org.mockito"     %   "mockito-core"  % "2.8.47"  % Test
)