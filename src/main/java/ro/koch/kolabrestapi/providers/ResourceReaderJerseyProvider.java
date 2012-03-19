package ro.koch.kolabrestapi.providers;

import static javax.ws.rs.core.MediaType.WILDCARD;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ro.koch.kolabrestapi.models.UnparsedResource;

@Provider
@Consumes(WILDCARD)
public class ResourceReaderJerseyProvider implements MessageBodyReader<UnparsedResource> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public UnparsedResource readFrom(Class<UnparsedResource> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        return new UnparsedResource(entityStream, mediaType);
    }
}
