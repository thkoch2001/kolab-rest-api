name := "kolab-rest-api"

version := "0.1"

organization := "ro.koch"

// scalaVersion := "2.9.0-1"

seq(webSettings: _*)

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

// Customize any further dependencies as desired
libraryDependencies ++= Seq(
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty,test", 
    "org.eclipse.jetty" % "jetty-server" % "7.4.5.v20110725",// % "compile,jetty",
    "org.eclipse.jetty" % "jetty-servlet" % "7.4.5.v20110725",// % "compile,jetty",
    "javax.ws.rs" % "jsr311-api" % "1.1.1",
    "com.sun.jersey" % "jersey-server" % "1.8",
    "com.sun.jersey" % "jersey-core" % "1.8",
//    "com.sun.jersey" % "jersey-json" % "1.8",
//    "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
    "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
)
