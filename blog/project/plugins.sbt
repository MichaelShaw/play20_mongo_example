// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "FuseSource Public Repository" at "http://repo.fusesource.com/nexus/content/repositories/public"


// Use the Play sbt plugin for Play projects

addSbtPlugin("play" % "sbt-plugin" % "2.0.2")

