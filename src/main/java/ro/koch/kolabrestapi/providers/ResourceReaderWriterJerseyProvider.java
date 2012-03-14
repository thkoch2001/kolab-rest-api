package ro.koch.kolabrestapi.providers;

import static javax.ws.rs.core.MediaType.WILDCARD;

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

import ro.koch.kolabrestapi.models.Resource;

import com.google.common.io.ByteStreams;
import com.google.common.io.OutputSupplier;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

@Provider
@Produces(WILDCARD) @Consumes(WILDCARD)
public class ResourceReaderWriterJerseyProvider extends AbstractMessageReaderWriterProvider<Resource> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> type, java.lang.reflect.Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Resource readFrom(Class<Resource> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        try {
            byte[] body = ByteStreams.toByteArray(entityStream);
            return new Resource(null, body, mediaType);
        } catch (final Exception e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public void writeTo(Resource resource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException {
        ByteStreams.write(resource.body,
                          new OutputSupplier<OutputStream>(){
              @Override public OutputStream getOutput() throws IOException {return entityStream;}
          }
        );
    }
}
