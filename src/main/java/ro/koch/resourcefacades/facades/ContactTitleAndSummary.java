package ro.koch.resourcefacades.facades;

import static com.google.common.base.Preconditions.checkNotNull;
import ro.koch.resourcefacades.Contact;
import ro.koch.resourcefacades.TitleAndSummary;

public class ContactTitleAndSummary implements TitleAndSummary {
    private final Contact contact;

    public ContactTitleAndSummary(Contact contact) {
        this.contact = checkNotNull(contact);
    }

    @Override
    public String getTitle() {
        return contact.getFirstName() + " "
             + contact.getLastName() + "<"
             + contact.getEmail() + ">";
    }

    @Override
    public String getSummary() {
        return getTitle() + "\n"
              + contact.getStreet() + "\n"
              + contact.getZipCode() + " "
              + contact.getCity();
    }

}
