package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.OK;
import static ro.koch.kolabrestapi.Routes.PathTemplate.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.COLLECTION;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.ext.history.FeedPagingHelper;
import org.apache.abdera2.model.Feed;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.PaginationRange;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.ResultList;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class Collection {
    private final Abdera abdera;
    private final PathParams pathParams;
    private final ConnectedStorage connectedStorage;
    private final String collection;
    private final LinkBuilder linkBuilder;

    @Inject
    public Collection(Abdera abdera, PathParams pathParams, ConnectedStorage storage, LinkBuilder linkBuilder) {
        this.pathParams = checkNotNull(pathParams);
        this.abdera = checkNotNull(abdera);
        this.connectedStorage = checkNotNull(storage);
        this.collection = pathParams.get(COLLECTION);
        this.linkBuilder = linkBuilder;
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam PaginationRange range, @InjectParam Preconditions preconditions) {
        ResultList resultList = storage().listUpdates(range, preconditions);
        ResponseBuilder rb = Response.status(resultList.status);

        if(resultList.status == OK) { // else is NOT_MODIFIED
            rb.entity(buildFeed(range, resultList))
              .tag(resultList.etag);
        }

        return rb.build();
    }

    private Feed buildFeed(PaginationRange range, ResultList resultList) {
        final Feed feed = abdera.newFeed();
        feed.setTitle(pathParams.get(AUTHORITY) + " sometitle " + collection);
        ResourceAbderaAdapter adapter = new ResourceAbderaAdapter(abdera, linkBuilder);
        for(final Resource resource : resultList.it) {
            adapter.addResourceToFeed(feed, resource);
        }
        if(range.moreToCome(resultList.total)) {
            FeedPagingHelper.setNext(feed, linkBuilder.next(range).toString());
        }
        return feed;
    }

    @POST
    public Response post(@InjectParam Clock clock,
                         Resource parsedResource) {
        Resource.Meta meta = new Resource.Meta(clock.get(), UUID.randomUUID().toString());
        Resource resource = new Resource(meta, parsedResource.body, parsedResource.mediaType);

        storage().post(resource.meta.id, resource);
        return Response.created(linkBuilder.entryUri(resource.meta.id)).build();
    }

    private CollectionStorage storage() {
        return connectedStorage.getConnectionStorage(collection);
    }
}
