package ro.koch.kolabrestapi.configuration

import _root_.com.google.inject.Guice
import _root_.com.google.inject.servlet.GuiceServletContextListener
import _root_.org.fusesource.scalate.guice.ScalateModule
import _root_.ro.koch.kolabrestapi.readers.ReadersGuiceModule
import _root_.ro.koch.resourcefacades.facades.GuiceModule

class ServletContextListener extends GuiceServletContextListener {
  def getInjector = {
    System.setProperty("scalate.mode","dev")
    Guice.createInjector(
        new ReadersGuiceModule,
        new JerseyGuiceModule,
        new GuiceModule,
        new KolabRestApiModule,
        new ScalateModule() {

        // TODO add some custom provider methods here
        // which can then be injected into resources or templates
        //
        // @Provides def createSomething = new MyThing()

        // lets add any package names which contain JAXRS resources
        override def resourcePackageNames
          = "ro.koch.kolabrestapi.providers" :: "ro.koch.kolabrestapi.resources" :: "org.fusesource.scalate.console" :: super.resourcePackageNames

        override def createResourceConfigProperties: Map[String, AnyRef] = {
          val answer = super.createResourceConfigProperties

          answer ++ Map(
            "com.sun.jersey.spi.container.ContainerRequestFilters" -> "com.sun.jersey.api.container.filter.LoggingFilter",
            "com.sun.jersey.spi.container.ContainerResponseFilters" -> "com.sun.jersey.api.container.filter.LoggingFilter",
            "com.sun.jersey.config.feature.Trace" -> "true",
            "com.sun.jersey.config.feature.FilterForwardOn404" -> "true",
            "com.sun.jersey.config.feature.ImplicitViewables" -> "true"
          )
        }
      })
  }
}
