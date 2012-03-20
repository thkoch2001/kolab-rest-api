package ro.koch.resourcefacades;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

public interface Reader<T> {
    boolean isReadable(MediaType mediaType);

    T readFrom(InputStream entityStream);

    Class<T> getParsedClass();
}
