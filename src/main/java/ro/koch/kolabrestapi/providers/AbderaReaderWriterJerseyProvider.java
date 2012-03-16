package ro.koch.kolabrestapi.providers;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_ATOMDELETED_XML;

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

import com.google.inject.Inject;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

@Provider
@Produces({APPLICATION_ATOM_XML,APPLICATION_XML,APPLICATION_ATOMDELETED_XML})
@Consumes(APPLICATION_ATOM_XML)
public class AbderaReaderWriterJerseyProvider extends AbstractMessageReaderWriterProvider<Base> {
    private final Abdera abdera;

    @Inject
    public AbderaReaderWriterJerseyProvider(Abdera abdera) {
        this.abdera = abdera;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> type, java.lang.reflect.Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return Base.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(Base t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        t.writeTo(entityStream);
    }

    @Override
    public Base readFrom(Class<Base> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return abdera.getParser().parse(entityStream);
    }
}
