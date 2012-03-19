package ro.koch.kolabrestapi.models;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Iterables.contains;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_CALENDAR_XML_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_POCO_JSON_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_VCARD_XML_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_CALENDAR_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_PLAIN_UTF8_TYPE;
import static ro.koch.kolabrestapi.MediaTypes.TEXT_VCARD_TYPE;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.google.common.base.Objects;

public class Collection {
    private final String title;
    private final String name;
    private final Iterable<MediaType> accepts;
    private final Iterable<Category> categories;

    public Collection(String name, String title, Iterable<MediaType> accepts, Iterable<Category> categories) {
        this.name = checkNotNull(name);
        this.title = checkNotNull(title);
        this.accepts = checkNotNull(accepts);
        this.categories = checkNotNull(categories);
    }

    public String getName() { return name; }
    public String getTitle() { return title; }
    public Iterable<MediaType> getAccepts() { return accepts; }
    public Iterable<Category> getCategories() { return categories; }

    @Override public String toString() {
        return toStringHelper(this)
            .add("name", name)
            .add("title", title)
            .toString();
    }

    @Override public boolean equals(Object obj) { return equal(this, obj); }

    @Override public int hashCode() {
        return Objects.hashCode(name, title);
    }

    public static enum TestCollections {
        calendar("calendar", "Kalender",
                of(APPLICATION_CALENDAR_XML_TYPE, TEXT_CALENDAR_TYPE),
                "calendar"),
        contacts("contacts", "Kontakte",
                of(APPLICATION_VCARD_XML_TYPE, TEXT_VCARD_TYPE, APPLICATION_POCO_JSON_TYPE),
                "contacts"),
        texts("texts", "Texte",
                of(TEXT_PLAIN_TYPE, TEXT_PLAIN_UTF8_TYPE), "texts")

        ;
        public final Collection collection;

        TestCollections(String name, String title, Iterable<MediaType> types, String category) {
            try {
                collection = new Collection(name, title, types,
                    of(new Category(category,new URI("http://koch.ro"),category))
                        );
            } catch (URISyntaxException e) {
                throw propagate(e);
            }
        }
    }

    public boolean isAcceptable(MediaType mediaType) {
        return contains(accepts, mediaType);
    }
}
