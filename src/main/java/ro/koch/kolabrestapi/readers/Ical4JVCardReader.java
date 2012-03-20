package ro.koch.kolabrestapi.readers;

import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.VCardBuilder;
import ro.koch.kolabrestapi.MediaTypes;
import ro.koch.resourcefacades.Reader;

public class Ical4JVCardReader implements Reader<VCard> {

    @Override public boolean isReadable(MediaType mediaType) {
        return MediaTypes.TEXT_VCARD_TYPE.equals(mediaType);
    }

    @Override public VCard readFrom(InputStream in) {
        try {
            return new VCardBuilder(in).build();
        } catch (IOException e) {
            throw propagate(e);
        } catch (ParserException e) {
            throw propagate(e);
        }
    }

    @Override
    public Class<VCard> getParsedClass() {
        return VCard.class;
    }
}
