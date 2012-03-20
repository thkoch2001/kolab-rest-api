package ro.koch.kolabrestapi.providers;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.WILDCARD;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import ro.koch.kolabrestapi.models.UnparsedResource;
import ro.koch.resourcefacades.FacadeRegistry;
import ro.koch.resourcefacades.Reader;

import com.google.inject.Inject;

@Provider
@Consumes(WILDCARD)
public class ResourceReaderJerseyProvider implements MessageBodyReader<UnparsedResource> {

    private final Set<Reader> readers;
    private final FacadeRegistry facadeRegistry;

    @Inject
    public ResourceReaderJerseyProvider(Set<Reader> readers,
                                        FacadeRegistry facadeRegistry) {
        this.readers = checkNotNull(readers);
        this.facadeRegistry = checkNotNull(facadeRegistry);
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return getReader(mediaType) != null;
    }

    @Override
    public UnparsedResource readFrom(
            Class<UnparsedResource> type,
            Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException
    {
        return new UnparsedResource(entityStream, mediaType, getReader(mediaType), facadeRegistry);
    }

    private Reader<? extends Object> getReader(MediaType mediaType) {
        for(Reader<? extends Object> reader : readers) {
            if(reader.isReadable(mediaType)) return reader;
        }
        return null;
    }
}
