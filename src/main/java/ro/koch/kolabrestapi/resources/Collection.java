package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;

import javax.ws.rs.GET;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.joda.time.DateTime;

import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.storage.CollectionStorage.StoredResource;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

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
        final Iterable<StoredResource> it = storage.getConnectionStorage(pathParams.get(COLLECTION))
                                                   .listUpdates(0, 20);
        feed.setTitle(pathParams.get(AUTHORITY) + " sometitle "+ pathParams.get(COLLECTION));
        for(final StoredResource stRes : it) {
            final Entry entry = abdera.newEntry();
            //entry.addAuthor(item.getAuthor());
            //entry.setTitle(item.getTitle());
            entry.setUpdated(new DateTime(stRes.updated));
            //entry.setSummary(item.getSummary());
            feed.addEntry(entry);
        }
        return feed;
    }

//    @POST
//    public Response post(@Context UriInfo info, VCard vcard) {
//        final String id = storage.createItem(pathParams.get(COLLECTION), vcard);
//        final URI uri = info.getAbsolutePathBuilder().path(id).build();
//        return Response.created(uri).build();
//    }

}
