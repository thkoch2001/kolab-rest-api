package ro.koch.kolabrestapi.configuration;

import static com.google.inject.Guice.createInjector;

import java.util.HashMap;
import java.util.Map;

import ro.koch.kolabrestapi.Routes;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

// http://jersey.java.net/nonav/apidocs/latest/contribs/jersey-guice/com/sun/jersey/guice/spi/container/servlet/package-summary.html
public class GuiceServletContextListenerImpl extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return createInjector(new JerseyServletModule() {

            @Override
            protected void configureServlets() {
                bind(Routes.class);

                final Map<String, String> params = new HashMap<String, String>();
                params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "ro.koch.kolabrestapi.providers");

                serve("/*").with(GuiceContainer.class, params);
            }
    });
}
}
