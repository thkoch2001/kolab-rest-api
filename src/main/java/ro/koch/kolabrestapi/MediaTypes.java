package ro.koch.kolabrestapi;

import static javax.ws.rs.core.MediaType.valueOf;

import javax.ws.rs.core.MediaType;

public class MediaTypes {
    public static final String TEXT_VCARD = "text/vcard";
    public static final MediaType TEXT_VCARD_TYPE = valueOf(TEXT_VCARD);

    public static final String APPLICATION_VCARD_XML = "application/vcard+xml";
    public static final MediaType APPLICATION_VCARD_XML_TYPE = valueOf(APPLICATION_VCARD_XML);

    public static final String APPLICATION_POCO_JSON = "application/poco+json";
    public static final MediaType APPLICATION_POCO_JSON_TYPE = valueOf(APPLICATION_POCO_JSON);

    public static final String APPLICATION_CALENDAR_XML = "application/calendar+xml";
    public static final MediaType APPLICATION_CALENDAR_XML_TYPE = valueOf(APPLICATION_CALENDAR_XML);

    public static final String TEXT_CALENDAR = "text/calendar";
    public static final MediaType TEXT_CALENDAR_TYPE = valueOf(TEXT_CALENDAR);

    public static final String APPLICATION_ATOMDELETED_XML = "application/atomdeleted+xml";
    public static final MediaType APPLICATION_ATOMDELETED_XML_TYPE = valueOf(APPLICATION_ATOMDELETED_XML);
}
