package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Feed;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

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

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Feed get(@InjectParam LinkBuilder linkBuilder) {
        final Feed feed = abdera.newFeed();
        final String collection = pathParams.get(COLLECTION);
        final Iterable<Resource> it = storage.getConnectionStorage(collection)
                                                   .listUpdates(0, 20);
        feed.setTitle(pathParams.get(AUTHORITY) + " sometitle "+ collection);
        for(final Resource resource : it) {
            feed.addEntry(
                    Entry.buildEntry(abdera, linkBuilder, collection, resource)
            );
        }
        return feed;
    }

    @POST
    public Response post(@InjectParam LinkBuilder linkBuilder,
                         @InjectParam Clock clock,
                         Resource parsedResource) {
        Resource.Meta meta = new Resource.Meta(clock.get(), UUID.randomUUID().toString());
        Resource resource = new Resource(meta, parsedResource.body, parsedResource.mediaType);

        storage.getConnectionStorage(pathParams.get(COLLECTION)).post(resource.meta.id, resource);
        return Response.created(linkBuilder.entryUri(pathParams.get(COLLECTION), resource.meta.id)).build();
    }

}
