package ro.koch.kolabrestapi.readers;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_PLAIN_UTF8_TYPE;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import ro.koch.resourcefacades.Reader;

import com.google.common.io.ByteStreams;

public class TextReader implements Reader<String> {

    @Override
    public boolean isReadable(MediaType mediaType) {
        return TEXT_PLAIN_TYPE.equals(mediaType)
            || TEXT_PLAIN_UTF8_TYPE.equals(mediaType);
    }

    @Override
    public String readFrom(InputStream in) {
        try {
            return new String(ByteStreams.toByteArray(in));
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public Class<String> getParsedClass() {
        return String.class;
    }

}
