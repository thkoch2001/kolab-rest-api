package ro.koch.resourcefacades.facades;

import java.io.IOException;
import java.io.OutputStream;

import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.VCardOutputter;

import com.google.common.base.Throwables;

public class Ical4JVCardWriter extends AbstractWriter<VCard> {
    private static final VCardOutputter outputter = new VCardOutputter(false);

    public Ical4JVCardWriter(VCard object) {
        super(object);
    }

    @Override public void writeTo(OutputStream out) {
        try {
            outputter.output(object, out);
        } catch (IOException e) {
            Throwables.propagate(e);
        } catch (ValidationException e) {
            Throwables.propagate(e);
        }
    }
}
