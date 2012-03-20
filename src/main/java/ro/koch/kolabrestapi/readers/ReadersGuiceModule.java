package ro.koch.kolabrestapi.readers;

import ro.koch.resourcefacades.Reader;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.*;

public class ReadersGuiceModule extends AbstractModule {

    @Override protected void configure() {
        @SuppressWarnings("rawtypes")
        Multibinder<Reader> multibinder
            = Multibinder.newSetBinder(binder(), Reader.class);
        // multibinder.addBinding().toInstance(new Twix());
        // multibinder.addBinding().toProvider(SnickersProvider.class);
        multibinder.addBinding().to(TextReader.class);
        multibinder.addBinding().to(Ical4JVCardReader.class);
    }

}
