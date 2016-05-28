name := "bible-parser"

version := "1.0"

scalaVersion := "2.11.8"

mainClass in (Compile,run) := Some("Parser")

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0"
)
    