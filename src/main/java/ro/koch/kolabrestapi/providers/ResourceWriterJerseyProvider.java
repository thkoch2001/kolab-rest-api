package ro.koch.kolabrestapi.providers;

import static javax.ws.rs.core.MediaType.WILDCARD;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import ro.koch.kolabrestapi.models.Resource;

@Provider
@Produces(WILDCARD)
public class ResourceWriterJerseyProvider implements MessageBodyWriter<Resource> {

    @Override
    public boolean isWriteable(Class<?> type, java.lang.reflect.Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return Resource.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(Resource resource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException {
        resource.asMediaType(mediaType, entityStream);
    }

    @Override
    public long getSize(Resource t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
