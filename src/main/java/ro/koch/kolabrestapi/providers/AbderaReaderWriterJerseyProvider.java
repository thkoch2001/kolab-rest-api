package ro.koch.kolabrestapi.providers;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Base;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.model.Service;

import com.google.inject.Inject;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

public abstract class AbderaReaderWriterJerseyProvider<T extends Base> extends AbstractMessageReaderWriterProvider<T> {
    private final Abdera abdera;

    protected AbderaReaderWriterJerseyProvider(Abdera abdera) {
        this.abdera = abdera;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> type, java.lang.reflect.Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return getClazz().isAssignableFrom(type);
    }

    protected abstract Class<?> getClazz();

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        t.writeTo(entityStream);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        final Document<Element> element = abdera.getParser().parse(entityStream);
        if(!getClazz().isAssignableFrom(getClazz())) throw new WebApplicationException();
        return (T) element;
    }

    @Provider
    @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    @Consumes(APPLICATION_ATOM_XML)
    public static class FeedProvider extends AbderaReaderWriterJerseyProvider<Feed> {
        protected static final Class<Feed> clazz = Feed.class;

        @Inject public FeedProvider(Abdera abdera) {
            super(abdera);
        }

        @Override
        protected Class<Feed> getClazz() { return clazz; }
    }

    @Provider
    @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    @Consumes(APPLICATION_ATOM_XML)
    public static class EntryProvider extends AbderaReaderWriterJerseyProvider<Entry> {
        protected static final Class<Entry> clazz = Entry.class;

        @Inject public EntryProvider(Abdera abdera) {
            super(abdera);
        }

        @Override
        protected Class<Entry> getClazz() { return clazz; }
    }

    @Provider
    @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    @Consumes(APPLICATION_ATOM_XML)
    public static class ServiceProvider extends AbderaReaderWriterJerseyProvider<Service> {
        protected static final Class<Service> clazz = Service.class;

        @Inject public ServiceProvider(Abdera abdera) {
            super(abdera);
        }

        @Override
        protected Class<Service> getClazz() { return clazz; }
    }
}
