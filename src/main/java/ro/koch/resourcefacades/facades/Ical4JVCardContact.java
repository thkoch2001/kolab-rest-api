package ro.koch.resourcefacades.facades;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;
import static net.fortuna.ical4j.vcard.Property.Id.ADR;
import static net.fortuna.ical4j.vcard.Property.Id.EMAIL;
import static net.fortuna.ical4j.vcard.Property.Id.N;
import static net.fortuna.ical4j.vcard.Property.Id.TEL;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.property.Address;
import ro.koch.resourcefacades.Contact;

public class Ical4JVCardContact implements Contact {
    private final VCard vcard;

    public Ical4JVCardContact(VCard vcard) {
        this.vcard = checkNotNull(vcard);
    }

    @Override
    public String getFirstName() {
        net.fortuna.ical4j.vcard.property.N property = (net.fortuna.ical4j.vcard.property.N) vcard.getProperty(N);
        if(null==property) return "No first name";
        return nullToEmpty(property.getGivenName());
    }

    @Override
    public String getLastName() {
        net.fortuna.ical4j.vcard.property.N property = (net.fortuna.ical4j.vcard.property.N) vcard.getProperty(N);
        if(null==property) return "No last name";
        return nullToEmpty(property.getGivenName());
    }

    @Override
    public String getEmail() {
        return getProperty(EMAIL);
    }

    @Override
    public String getPhone() {
        return getProperty(TEL);
    }

    @Override
    public String getStreet() {
        Address property = (Address) vcard.getProperty(ADR);
        if(null==property) return "No Address";
        return nullToEmpty(property.getStreet());
    }

    @Override
    public String getCity() {
        Address property = (Address) vcard.getProperty(ADR);
        if(null==property) return "No Address";
        return nullToEmpty(property.getLocality());
    }

    @Override
    public String getZipCode() {
        Address property = (Address) vcard.getProperty(ADR);
        if(null==property) return "No Address";
        return nullToEmpty(property.getPostcode());
    }

    private String getProperty(Property.Id propertyId) {
        Property property = vcard.getProperty(propertyId);
        if(null==property) return "No " + propertyId.name();
        return nullToEmpty(property.getValue());
    }
}
