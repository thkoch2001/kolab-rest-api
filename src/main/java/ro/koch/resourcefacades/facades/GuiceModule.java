package ro.koch.resourcefacades.facades;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_PLAIN_UTF8_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_VCARD_TYPE;

import javax.ws.rs.core.MediaType;

import net.fortuna.ical4j.vcard.VCard;
import ro.koch.resourcefacades.FacadeRegistry;
import ro.koch.resourcefacades.facades.AbstractWriter.WriterFactory;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO Auto-generated method stub

    }

    @Provides
    @Singleton
    public FacadeRegistry facadeRegistry() {
        FacadeRegistry.Builder builder = new FacadeRegistry.Builder();
        put(builder, Ical4JVCardWriter.class, VCard.class, TEXT_VCARD_TYPE);
        put(builder, TextWriter.class, String.class, TEXT_PLAIN_TYPE);
        put(builder, TextWriter.class, String.class, TEXT_PLAIN_UTF8_TYPE);
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(FacadeRegistry.Builder builder,
                             Class<T> classToBuild,
                             Class<? extends Object> dependentClass,
                             MediaType mediaType) {
        builder.put(ro.koch.resourcefacades.Writer.class,
                    new WriterFactory(
                            classToBuild,
                            dependentClass,
                            ImmutableSet.of(mediaType)));
    }
}
