package ro.koch.kolabrestapi.configuration;

import static com.google.inject.Guice.createInjector;

import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.Abdera;

import ro.koch.kolabrestapi.Routes;
import ro.koch.kolabrestapi.storage.Storages;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

// http://jersey.java.net/nonav/apidocs/latest/contribs/jersey-guice/com/sun/jersey/guice/spi/container/servlet/package-summary.html
public class GuiceServletContextListenerImpl extends GuiceServletContextListener {
    private final ImmutableList.Builder<String> jerseyPackages = ImmutableList.builder();

    @Override
    protected Injector getInjector() {
        return createInjector(new JerseyServletModule() {

            @Override
            protected void configureServlets() {
                bind(Routes.class);
                bind(Abdera.class).asEagerSingleton();
                bind(Storages.class).asEagerSingleton();

                jerseyPackages.add("ro.koch.kolabrestapi.providers");

                serve("/*").with(GuiceContainer.class, getContainerParams());
            }
        });
    }

    private Map<String, String> getContainerParams() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
                Joiner.on(";").join(jerseyPackages.build()));
        params.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
        params.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
        params.put("com.sun.jersey.config.feature.Trace", "true");

        return params;
    }
}
