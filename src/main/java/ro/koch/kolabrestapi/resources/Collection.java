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
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.ResultList;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class Collection {
    private final Abdera abdera;
    private final LinkBuilder linkBuilder;
    private final CollectionStorage storage;

    @Inject
    public Collection(Abdera abdera, CollectionStorage storage, LinkBuilder linkBuilder) {
        this.abdera = checkNotNull(abdera);
        this.storage = checkNotNull(storage);
        this.linkBuilder = checkNotNull(linkBuilder);
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam PaginationRange range,
                        @InjectParam Preconditions preconditions) {
        ResultList resultList = storage.listUpdates(range, preconditions);
        ResponseBuilder rb = Response.status(resultList.status);

        if(resultList.status == OK) { // else is NOT_MODIFIED
            rb.entity(buildFeed(range, resultList))
              .tag(resultList.etag);
        }

        return rb.build();
    }

    private Feed buildFeed(PaginationRange range, ResultList resultList) {
        final Feed feed = abdera.newFeed();
        feed.setTitle(linkBuilder.getParam(AUTHORITY) + "'s collection of " + linkBuilder.getParam(COLLECTION));
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

        storage.post(resource.meta.id, resource);
        return Response.created(linkBuilder.entryUri(resource.meta.id)).build();
    }
}
