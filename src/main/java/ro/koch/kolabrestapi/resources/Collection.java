package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.fortuna.ical4j.vcard.VCard;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.VCardListItem;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

public class Collection {
    private final Abdera abdera;
    private final PathParams pathParams;
    private final ConnectedStorage storage;

    @Inject
    public Collection(Abdera abdera, PathParams pathParams, ConnectedStorage storage) {
        this.pathParams = checkNotNull(pathParams);
        this.abdera = checkNotNull(abdera);
        this.storage = checkNotNull(storage);
    }

    @GET
    public Feed get() {
        final Feed feed = abdera.newFeed();
        final ImmutableList<VCardListItem> items = storage.getItems(pathParams.get(COLLECTION), 0, -1);
        feed.setTitle(pathParams.get(AUTHORITY) + " sometitle "+ pathParams.get(COLLECTION));
        for(final VCardListItem item : items) {
            final Entry entry = abdera.newEntry();
            entry.addAuthor(item.getAuthor());
            entry.setTitle(item.getTitle());
            entry.setUpdated(item.getUpdated());
            entry.setSummary(item.getSummary());
            feed.addEntry(entry);
        }
        return feed;
    }

    @POST
    public Response post(@Context UriInfo info, VCard vcard) {
        final String id = storage.createItem(pathParams.get(COLLECTION), vcard);
        final URI uri = info.getAbsolutePathBuilder().path(id).build();
        return Response.created(uri).build();
    }

}
