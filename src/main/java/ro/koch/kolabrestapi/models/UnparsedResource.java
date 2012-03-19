package ro.koch.kolabrestapi.models;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import ro.koch.kolabrestapi.models.Resource.Meta;

import com.google.common.io.ByteStreams;

public class UnparsedResource {
    private final InputStream in;
    private final MediaType mediaType;

    public UnparsedResource(InputStream in, MediaType mediaType) {
        this.in = checkNotNull(in);
        this.mediaType = checkNotNull(mediaType);
    }

    public Resource parse(Meta meta) {
        byte[] body;
        try {
            body = ByteStreams.toByteArray(in);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
        return new Resource(meta, body, mediaType);
    }

    public InputStream getIn() { return in; }
    public MediaType getMediaType() { return mediaType; }
}
