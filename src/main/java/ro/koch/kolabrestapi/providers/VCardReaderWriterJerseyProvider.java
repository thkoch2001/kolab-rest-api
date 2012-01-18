package ro.koch.kolabrestapi.providers;

import static ro.koch.kolabrestapi.MediaTypes.TEXT_VCARD;

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

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.VCardBuilder;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

@Provider @Produces(TEXT_VCARD) @Consumes(TEXT_VCARD)
public class VCardReaderWriterJerseyProvider extends AbstractMessageReaderWriterProvider<VCard> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> type, java.lang.reflect.Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return VCard.class.isAssignableFrom(type);
    }

    @Override
    public VCard readFrom(Class<VCard> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        try {
            return (new VCardBuilder(entityStream)).build();
        } catch (final ParserException e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public void writeTo(VCard vcard, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        writeToAsString(vcard.toString(), entityStream, mediaType);
    }

}
