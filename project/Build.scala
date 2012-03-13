import sbt._
import com.github.siasia._
import WebPlugin._
import PluginKeys._
import Keys._

object Build extends sbt.Build {

  import Dependencies._

  lazy val myProject = Project("kolab-rest-api", file("."))
    .settings(WebPlugin.webSettings: _*)
    .settings(
      version := "0.1",
      organization := "ro.koch",
     // scalaVersion := "2.9.0-1"
      resolvers ++= resolutionRepos,
      libraryDependencies ++= myLibDependencies
    )

}

object Dependencies {
  val resolutionRepos = Seq(
    "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
    "Local Maven repo" at "file://"+Path.userHome+"/.m2/repository"
  )

  object V {
    val jersey  = "1.12"
    val abdera  = "2.0-SNAPSHOT"
    val scalate  = "[1.5.3,)"
  //    val jetty   = "8.1.0.v20120127"
  //    val slf4j   = "1.6.4"
  //    val logback = "1.0.0"
  }

  // Customize any further dependencies as desired
  val myLibDependencies = Seq(
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "container",
    "javax.ws.rs" % "jsr311-api" % "1.1.1",
    "com.sun.jersey" % "jersey-server" % V.jersey,
    "com.sun.jersey" % "jersey-core" % V.jersey,
    "com.sun.jersey" % "jersey-servlet" % V.jersey,
    "com.sun.jersey.contribs" % "jersey-guice" % V.jersey,
    "com.google.inject" % "guice" % "[3.0,)",
    "com.google.guava" % "guava" % "[11.0.1,)",
    "org.apache.abdera2" % "abdera2-core" % V.abdera,
    "org.apache.abdera2" % "abdera2-common" % V.abdera,
    "org.apache.abdera2" % "abdera2-ext" % V.abdera,
    "org.mnode.ical4j" % "ical4j-vcard" % "[0.9.0,)",
    "org.fusesource.scalate" % "scalate-guice" % V.scalate,
    "org.fusesource.scalate" % "scalate-page" % V.scalate
  //    "com.sun.jersey" % "jersey-json" % "1.11",
  //    "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
  //    "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  )
}


