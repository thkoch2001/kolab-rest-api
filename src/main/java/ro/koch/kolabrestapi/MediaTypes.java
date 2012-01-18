package ro.koch.kolabrestapi;

import javax.ws.rs.core.MediaType;

public class MediaTypes {
    public static final String TEXT_VCARD = "text/vcard";
    public static final MediaType TEXT_VCARD_TYPE = MediaType.valueOf(TEXT_VCARD);

    public static final String APPLICATION_VCARD_XML = "application/vcard+xml";
    public static final MediaType APPLICATION_VCARD_XML_TYPE = MediaType.valueOf(APPLICATION_VCARD_XML);
}
