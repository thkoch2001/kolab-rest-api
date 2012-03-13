package ro.koch.kolabrestapi.configuration;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.MessageBodyWorkers;
import com.sun.jersey.spi.container.ExceptionMapperContext;
import com.sun.jersey.spi.container.WebApplication;

// copied from com.sun.jersey.guice.JerseyServletModule
public class JerseyGuiceModule extends AbstractModule {
    @Provides
    public WebApplication webApp(GuiceContainer guiceContainer) {
        return guiceContainer.getWebApplication();
    }

    @Provides
    public Providers providers(WebApplication webApplication) {
        return webApplication.getProviders();
    }

    @Provides
    public FeaturesAndProperties fearturesAndProperties(WebApplication webApplication) {
        return webApplication.getFeaturesAndProperties();
    }

    @Provides
    public MessageBodyWorkers messageBodyWorkers(WebApplication webApplication) {
        return webApplication.getMessageBodyWorkers();
    }

    @Provides
    public ExceptionMapperContext exceptionMapperContext(WebApplication webApplication) {
        return webApplication.getExceptionMapperContext();
    }

    @RequestScoped
    @Provides
    public HttpContext httpContext(WebApplication webApplication) {
        return webApplication.getThreadLocalHttpContext();
    }

    @Provides
    @RequestScoped
    public UriInfo uriInfo(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getUriInfo();
    }

    @Provides
    @RequestScoped
    public ExtendedUriInfo extendedUriInfo(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getUriInfo();
    }

    @RequestScoped
    @Provides
    public HttpRequestContext requestContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public HttpHeaders httpHeaders(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public Request request(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public SecurityContext securityContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public HttpResponseContext responseContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getResponse();
    }

    @Override
    protected void configure() {
        // TODO Auto-generated method stub

    }
}
