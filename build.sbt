name := "kolab-rest-api"

version := "0.1"

organization := "ro.koch"

// scalaVersion := "2.9.0-1"

seq(webSettings :_*)

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

// Customize any further dependencies as desired
libraryDependencies ++= Seq(
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "container",
    "javax.ws.rs" % "jsr311-api" % "1.1.1",
    "com.sun.jersey" % "jersey-server" % "1.11",
    "com.sun.jersey" % "jersey-core" % "1.11",
    "com.sun.jersey" % "jersey-servlet" % "1.11",
    "com.sun.jersey.contribs" % "jersey-guice" % "1.11",
    "com.google.inject" % "guice" % "3.0",
    "com.google.guava" % "guava" % "11.0.1",
    "org.apache.abdera" % "abdera-server" % "[1.1.2,)",
    "org.mnode.ical4j" % "ical4j-vcard" % "[0.9.0,)"
//    "com.sun.jersey" % "jersey-json" % "1.11",
//    "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
//    "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
)
