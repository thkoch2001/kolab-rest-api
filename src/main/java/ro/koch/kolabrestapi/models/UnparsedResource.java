package ro.koch.kolabrestapi.models;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import ro.koch.kolabrestapi.models.Resource.Meta;
import ro.koch.resourcefacades.FacadeRegistry;
import ro.koch.resourcefacades.FacadesProvider;
import ro.koch.resourcefacades.Reader;

import com.google.common.collect.MutableClassToInstanceMap;

public class UnparsedResource<T> {
    private final InputStream in;
    private final MediaType mediaType;
    private final Reader<T> reader;
    private final FacadeRegistry facadeRegistry;

    public UnparsedResource(InputStream in, MediaType mediaType, Reader<T> reader, FacadeRegistry facadeRegistry) {
        this.in = checkNotNull(in);
        this.mediaType = checkNotNull(mediaType);
        this.reader = checkNotNull(reader);
        this.facadeRegistry = checkNotNull(facadeRegistry);
    }

    public Resource parse(Meta meta) {
        MutableClassToInstanceMap<Object> inputFacades = MutableClassToInstanceMap.create();
        inputFacades.putInstance(reader.getParsedClass(), reader.readFrom(in));
        FacadesProvider facadesProvider = new FacadesProvider(facadeRegistry, inputFacades);
        return new Resource(meta, facadesProvider, mediaType);
    }

    public MediaType getMediaType() { return mediaType; }
}
