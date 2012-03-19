package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.PaginationRange;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.models.Resource.Meta;
import ro.koch.kolabrestapi.models.UnparsedResource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.ResultList;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class Collection {
    private final LinkBuilder linkBuilder;
    private final CollectionStorage storage;

    @Inject
    public Collection(CollectionStorage storage, LinkBuilder linkBuilder) {
        this.storage = checkNotNull(storage);
        this.linkBuilder = checkNotNull(linkBuilder);
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam ResourceAbderaAdapter abderaAdapter,
                        @InjectParam PaginationRange range,
                        @InjectParam Preconditions preconditions)
    {
        ResultList resultList = storage.listUpdates(range, preconditions);
        ResponseBuilder rb = Response.status(resultList.status);

        if(resultList.status == OK) { // else is NOT_MODIFIED
            rb.entity(abderaAdapter.buildFeed(range, resultList))
              .tag(resultList.etag);
        }

        return rb.build();
    }

    @POST
    public Response post(@InjectParam Clock clock,
                         @InjectParam ro.koch.kolabrestapi.models.Collection collection,
                         UnparsedResource unparsedResource)
    {
        if(!collection.isAcceptable(unparsedResource.getMediaType())) {
            // TODO list acceptable variants
            return Response.notAcceptable(null).build();
        }
        return Response.created(
                linkBuilder.entryUri(
                 storage.post(
                  unparsedResource.parse(new Meta(clock.get(), UUID.randomUUID().toString()))
               ))).build();
    }
}
