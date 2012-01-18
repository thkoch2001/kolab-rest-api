package ro.koch.kolabrestapi.models;

import static com.google.common.base.Preconditions.checkNotNull;
import net.fortuna.ical4j.vcard.Property.Id;
import net.fortuna.ical4j.vcard.VCard;

public class VCardListItem implements ListItem<VCard> {
    private final VCard vcard;

    public VCardListItem(VCard vcard) {
        this.vcard = checkNotNull(vcard);
    }

    @Override
    public String getTitle() {
        return vcard.getProperty(Id.FN).getValue();
    }

    @Override
    public String getSummary() {
        return "summary?";
    }

    @Override
    public String getAuthor() {
        return "autor?";
    }

    @Override
    public String getUpdated() {
        return "updated?";
    }

    @Override
    public String getId() {
        return vcard.getProperty(Id.UID).getValue();
    }

    @Override
    public VCard getPayload() {
        return vcard;
    }

}
